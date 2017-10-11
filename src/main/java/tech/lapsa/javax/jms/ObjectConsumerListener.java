package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public abstract class ObjectConsumerListener<T extends Serializable> implements MessageListener {

    private final Class<T> objectClazz;

    protected ObjectConsumerListener(final Class<T> objectClazz) {
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
	    ObjectMessage requestMessage = (ObjectMessage) message;

	    if (!requestMessage.isBodyAssignableTo(objectClazz)) {
		logger.log(Level.SEVERE,
			String.format("Invalid body type. %1$s object is expected.", objectClazz.getCanonicalName()));
		return;
	    }
	    T t = requestMessage.getBody(objectClazz);
	    try {
		accept(t);
	    } catch (Throwable e) {
		logger.log(Level.WARNING, "Exception", e);
	    }
	} catch (JMSException e) {
	    logger.log(Level.SEVERE, e.getMessage(), e);
	}
    }

    protected abstract void accept(T object);
}
