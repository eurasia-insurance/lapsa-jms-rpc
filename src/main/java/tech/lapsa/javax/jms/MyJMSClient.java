package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.validation.ValidationException;

import tech.lapsa.javax.jms.MyJMSFunctions.InvalidResponseTypeException;
import tech.lapsa.javax.jms.MyJMSFunctions.ResponseNotReceivedException;

public interface MyJMSClient {

    <T extends Serializable> MyJMSConsumer<T> createConsumer(Destination destination);

    <T extends Serializable> MyJMSMultipleConsumer<T> createMultipleConsumer(Destination destination)
	    throws JMSException;

    <T extends Serializable, R extends Serializable> MyJMSFunction<T, R> createFunction(final Destination destination,
	    Class<R> rClass);

    public static interface MyJMSFunction<T extends Serializable, R extends Serializable> {

	R apply(T inObject) throws JMSException, RuntimeException, ValidationException, ResponseNotReceivedException,
		InvalidResponseTypeException;

	R apply(T inObject, Properties properties)
		throws JMSException, RuntimeException, ValidationException, ResponseNotReceivedException,
		InvalidResponseTypeException;
    }

    public static interface MyJMSConsumer<T extends Serializable> {

	void accept(T inObject)
		throws JMSException, ValidationException, RuntimeException, ResponseNotReceivedException,
		InvalidResponseTypeException;

	void accept(T inObject, Properties properties)
		throws JMSException, ValidationException, RuntimeException, ResponseNotReceivedException,
		InvalidResponseTypeException;

	void acceptNoWait(T inObject) throws JMSException;

	void acceptNoWait(T inObject, Properties properties) throws JMSException;
    }

    @SuppressWarnings("unchecked")
    public static interface MyJMSMultipleConsumer<T extends Serializable> extends AutoCloseable {
	void acceptNoWait(T... inObjects) throws JMSException;

	@Override
	void close() throws JMSException;
    }
}
