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

public abstract class ObjectFunctionListener<T extends Serializable, R extends Serializable>
	implements MessageListener {

    private final Class<T> objectClazz;

    protected ObjectFunctionListener(final Class<T> objectClazz) {
	this.objectClazz = objectClazz;
    }

    @Override
    public void onMessage(Message message) {
	Logger logger = Logger.getLogger(this.getClass().getPackage().getName());
	try {
	    if (!(message instanceof ObjectMessage)) {
		logger.log(Level.SEVERE, "Invalid message type. javax.jms.ObjectMessage is expected.");
		return;
	    }

	    ObjectMessage request = (ObjectMessage) message;

	    if (!request.isBodyAssignableTo(objectClazz)) {
		logger.log(Level.SEVERE,
			String.format("Invalid body type. %1$s object is expected.", objectClazz.getCanonicalName()));
		return;
	    }

	    try {
		T t = request.getBody(objectClazz);
		R r = apply(t);
		if (request.getJMSReplyTo() != null) {
		    try (Connection connection = getConnection();
			    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
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

    protected abstract Connection getConnection();

    protected abstract R apply(T t);
}
