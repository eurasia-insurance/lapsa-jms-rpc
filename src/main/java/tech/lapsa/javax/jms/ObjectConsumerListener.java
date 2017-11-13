package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageFormatException;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import tech.lapsa.java.commons.logging.MyLogger;

public abstract class ObjectConsumerListener<T extends Serializable> implements MessageListener {

    private final Class<T> objectClazz;

    protected ObjectConsumerListener(final Class<T> objectClazz) {
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
		String v;
		try {
		    v = request.getStringProperty(k);
		    p.setProperty(k, v);
		} catch (MessageFormatException ignored) {
		}
	    }

	    T t = request.getBody(objectClazz);
	    try {
		accept(t, p);
	    } catch (RuntimeException e) {
		logger.WARNING.log(e, "Runtime exception occured while processing the object");
	    }
	} catch (JMSException e) {
	    logger.SEVERE.log(e);
	}
    }

    protected abstract void accept(T t, Properties properties);
}
