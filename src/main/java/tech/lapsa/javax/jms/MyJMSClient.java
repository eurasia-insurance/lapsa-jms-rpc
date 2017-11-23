package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.validation.ValidationException;

public interface MyJMSClient {

    //

    <T extends Serializable> MyJMSConsumer<T> createConsumer(Destination destination);

    <T extends Serializable> MyJMSConsumer<T> createQueueConsumer(String queuePhysicalName);

    <T extends Serializable> MyJMSConsumer<T> createTopicConsumer(String topicPhysicalName);

    //

    <T extends Serializable> MyJMSMultipleConsumer<T> createMultipleConsumer(Destination destination)
	    throws JMSException;

    <T extends Serializable> MyJMSMultipleConsumer<T> createMultipleQueueConsumer(String queuePhysicalName)
	    throws JMSException;

    <T extends Serializable> MyJMSMultipleConsumer<T> createMultipleTopicConsumer(String topicPhysicalName)
	    throws JMSException;

    //

    <T extends Serializable, R extends Serializable> MyJMSFunction<T, R> createFunction(Destination destination,
	    Class<R> rClass);

    <T extends Serializable, R extends Serializable> MyJMSFunction<T, R> createQueueFunction(String queuePhysicalName,
	    Class<R> rClass);

    <T extends Serializable, R extends Serializable> MyJMSFunction<T, R> createTopicFunction(String topicPhysicalName,
	    Class<R> rClass);

    //

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
