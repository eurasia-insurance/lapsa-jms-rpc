package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;

import javax.jms.Destination;
import javax.jms.JMSException;

public interface JmsClient {

    //

    <E extends Serializable> JmsConsumer<E> createConsumer(Destination destination);

    <E extends Serializable> JmsConsumer<E> createConsumerQueue(String queuePhysicalName);

    <E extends Serializable> JmsConsumer<E> createConsumerTopic(String topicPhysicalName);

    public static interface JmsConsumer<E extends Serializable> {

	void accept(E entity) throws JMSException;

	void accept(E entity, Properties properties) throws JMSException;
    }

    //

    <E extends Serializable> JmsSender<E> createSender(Destination destination)
	    throws JMSException;

    <E extends Serializable> JmsSender<E> createSenderQueue(String queuePhysicalName);

    <E extends Serializable> JmsSender<E> createSenderTopic(String topicPhysicalName);

    public static interface JmsSender<E extends Serializable> extends AutoCloseable {

	void send(E entity, Properties properties) throws JMSException;

	@SuppressWarnings("unchecked")
	void send(E... entities) throws JMSException;

	@Override
	void close() throws JMSException;
    }

    //

    <E extends Serializable, R extends Serializable> JmsCallable<E, R> createCallable(Destination destination,
	    Class<R> resultClazz);

    <E extends Serializable, R extends Serializable> JmsCallable<E, R> createCallableQueue(String queuePhysicalName,
	    Class<R> resultClazz);

    <E extends Serializable, R extends Serializable> JmsCallable<E, R> createCallableTopic(String topicPhysicalName,
	    Class<R> resultClazz);

    public static interface JmsCallable<E extends Serializable, R extends Serializable> {

	R call(E entity) throws JMSException;

	R call(E entity, Properties properties) throws JMSException;
    }

}
