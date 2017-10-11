package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public interface ObjectConsumerListener<T extends Serializable> extends MessageListener {

    @Override
    default void onMessage(Message message) {
	Logger logger = Logger.getLogger(this.getClass().getPackage().getName());
	try {
	    if (!(message instanceof ObjectMessage)) {
		logger.log(Level.SEVERE, "Invalid message type. javax.jms.ObjectMessage is expected.");
		return;
	    }
	    Class<T> clazz = getObjectClazz();

	    ObjectMessage requestMessage = (ObjectMessage) message;

	    if (!requestMessage.isBodyAssignableTo(clazz)) {
		logger.log(Level.SEVERE,
			String.format("Invalid body type. %1$s object is expected.", clazz.getCanonicalName()));
		return;
	    }
	    T t = requestMessage.getBody(clazz);
	    try {
		accept(t);
	    } catch (Throwable e) {
		logger.log(Level.WARNING, "Exception", e);
	    }
	} catch (JMSException e) {
	    logger.log(Level.SEVERE, e.getMessage(), e);
	}
    }

    Class<T> getObjectClazz();

    void accept(T object);

}
