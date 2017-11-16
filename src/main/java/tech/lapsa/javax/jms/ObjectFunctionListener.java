package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageFormatException;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import tech.lapsa.java.commons.logging.MyLogger;

public abstract class ObjectFunctionListener<T extends Serializable, R extends Serializable>
	implements MessageListener {

    private final Class<T> objectClazz;

    protected ObjectFunctionListener(final Class<T> objectClazz) {
	this.objectClazz = objectClazz;
    }

    private MyLogger logger = MyLogger.newBuilder() //
	    .withNameOf(this.getClass()) //
	    .build();

    @Override
    public void onMessage(Message message) {
	try {
	    if (!(message instanceof ObjectMessage)) {
		logger.SEVERE.log("Invalid message type. javax.jms.ObjectMessage is expected.");
		return;
	    }

	    ObjectMessage request = (ObjectMessage) message;

	    if (!request.isBodyAssignableTo(objectClazz)) {
		logger.SEVERE.log("Invalid body type. %1$s object is expected.", objectClazz.getCanonicalName());
		return;
	    }

	    Properties p = new Properties();
	    Enumeration<?> en = request.getPropertyNames();
	    while (en.hasMoreElements()) {
		String k = en.nextElement().toString();
		if (k.startsWith("JMS"))
		    continue;
		String v;
		try {
		    v = request.getStringProperty(k);
		    p.setProperty(k, v);
		} catch (MessageFormatException ignored) {
		}
	    }

	    T t = request.getBody(objectClazz);
	    try {
		R r = apply(t, p);
		if (request.getJMSReplyTo() != null) {
		    try (Connection connection = newConnection();
			    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			    MessageProducer producer = session.createProducer(request.getJMSReplyTo())) {
			Message reply = session.createObjectMessage(r);
			reply.setJMSCorrelationID(request.getJMSMessageID());
			producer.send(reply);
			logger.FINE.log("Reply message %1$s sent", reply.getClass().getSimpleName());
		    }
		}
	    } catch (RuntimeException e) {
		logger.WARN.log(e, "Runtime exception occured while processing the object");
	    }

	} catch (JMSException e) {
	    logger.SEVERE.log(e);
	}
    }

    protected abstract Connection newConnection();

    protected abstract R apply(T t, Properties properties);
}
