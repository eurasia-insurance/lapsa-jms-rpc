package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

public interface ObjectFunctionListener<T extends Serializable, R extends Serializable> extends MessageListener {

    @Override
    default void onMessage(Message message) {
	Logger logger = Logger.getLogger(this.getClass().getPackage().getName());
	try {
	    if (!(message instanceof ObjectMessage)) {
		logger.log(Level.SEVERE, "Invalid message type. javax.jms.ObjectMessage is expected.");
		return;
	    }
	    Class<T> clazz = getObjectClazz();
	    ObjectMessage request = (ObjectMessage) message;

	    if (!request.isBodyAssignableTo(clazz)) {
		logger.log(Level.SEVERE,
			String.format("Invalid body type. %1$s object is expected.", clazz.getCanonicalName()));
		return;
	    }

	    try {
		T t = request.getBody(clazz);
		R r = apply(t);
		if (request.getJMSReplyTo() != null) {
		    try (Session session = getConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);
			    MessageProducer producer = session.createProducer(request.getJMSReplyTo())) {
			Message reply = session.createObjectMessage(r);
			reply.setJMSCorrelationID(request.getJMSMessageID());
			producer.send(reply);
			logger.fine(String.format("Reply message %1$s sent", reply.getClass().getSimpleName()));
		    }
		}
	    } catch (Throwable e) {
		logger.log(Level.WARNING, "Exception", e);
	    }
	} catch (JMSException e) {
	    logger.log(Level.SEVERE, e.getMessage(), e);
	}
    }

    Class<T> getObjectClazz();

    Connection getConnection();

    R apply(T t);
}
