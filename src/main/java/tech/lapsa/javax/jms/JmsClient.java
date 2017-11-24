package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;

import javax.jms.Destination;
import javax.jms.JMSException;

public interface MyJMSClient {

    //

    <E extends Serializable> MyJMSConsumer<E> createConsumer(Destination destination);

    <E extends Serializable> MyJMSConsumer<E> createQueueConsumer(String queuePhysicalName);

    <E extends Serializable> MyJMSConsumer<E> createTopicConsumer(String topicPhysicalName);

    public static interface MyJMSConsumer<E extends Serializable> {

	void accept(E entity) throws JMSException;

	void accept(E entity, Properties properties) throws JMSException;

	void acceptNoWait(E entity) throws JMSException;

	void acceptNoWait(E entity, Properties properties) throws JMSException;
    }

    //

    <E extends Serializable> MyJMSMultipleConsumer<E> createMultipleConsumer(Destination destination)
	    throws JMSException;

    <E extends Serializable> MyJMSMultipleConsumer<E> createMultipleQueueConsumer(String queuePhysicalName)
	    throws JMSException;

    <E extends Serializable> MyJMSMultipleConsumer<E> createMultipleTopicConsumer(String topicPhysicalName)
	    throws JMSException;

    @SuppressWarnings("unchecked")
    public static interface MyJMSMultipleConsumer<E extends Serializable> extends AutoCloseable {
	void acceptNoWait(E... entities) throws JMSException;

	@Override
	void close() throws JMSException;
    }

    //

    <E extends Serializable, R extends Serializable> MyJMSFunction<E, R> createFunction(Destination destination,
	    Class<R> resultClazz);

    <E extends Serializable, R extends Serializable> MyJMSFunction<E, R> createQueueFunction(String queuePhysicalName,
	    Class<R> resultClazz);

    <E extends Serializable, R extends Serializable> MyJMSFunction<E, R> createTopicFunction(String topicPhysicalName,
	    Class<R> resultClazz);

    public static interface MyJMSFunction<E extends Serializable, R extends Serializable> {

	R call(E entity) throws JMSException;

	R call(E entity, Properties properties) throws JMSException;
    }

}
