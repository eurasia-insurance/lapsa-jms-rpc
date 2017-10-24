package tech.lapsa.javax.jms;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
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
	    T t = request.getBody(objectClazz);
	    try {
		accept(t);
	    } catch (RuntimeException e) {
		logger.WARNING.log(e, "Runtime exception occured while processing the object");
	    }
	} catch (JMSException e) {
	    logger.SEVERE.log(e);
	}
    }

    protected abstract void accept(T object);
}
