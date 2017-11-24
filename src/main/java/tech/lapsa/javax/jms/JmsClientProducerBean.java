package tech.lapsa.javax.jms;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jms.Destination;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;

@Singleton
public class JmsClientProducerBean {

    @Inject
    @JMSConnectionFactory(Constants.JNDI_DEFAULT_JMS_CONNECTION_FACTORY)
    private JMSContext context;

    private JmsClient instance;

    @PostConstruct
    public void init() {
	instance = new MyJMSClientImpl();
    }

    @Produces
    public JmsClient getInstance() {
	return instance;
    }

    private class MyJMSClientImpl implements JmsClient {

	@Override
	public <E extends Serializable> JmsConsumer<E> createConsumer(final Destination destination) {
	    return JmsClients.createConsumer(context, destination);
	}

	@Override
	public <E extends Serializable> JmsConsumer<E> createConsumerQueue(final String queuePhysicalName) {
	    return JmsClients.createConsumerQueue(context, queuePhysicalName);
	}

	@Override
	public <E extends Serializable> JmsConsumer<E> createConsumerTopic(final String topicPhysicalName) {
	    return JmsClients.createConsumerTopic(context, topicPhysicalName);
	}

	//

	@Override
	public <E extends Serializable> JmsMultipleConsumer<E> createMultipleConsumer(final Destination destination)
		throws JMSException {
	    return JmsClients.createMultipleConsumer(context, destination);
	}

	@Override
	public <E extends Serializable> JmsMultipleConsumer<E> createMultipleConsumerQueue(
		final String queuePhysicalName)
		throws JMSException {
	    return JmsClients.createMultipleConsumerQueue(context, queuePhysicalName);
	}

	@Override
	public <E extends Serializable> JmsMultipleConsumer<E> createMultipleConsumerTopic(
		final String topicPhysicalName)
		throws JMSException {
	    return JmsClients.createMultipleConsumerTopic(context, topicPhysicalName);
	}

	//

	@Override
	public <E extends Serializable, R extends Serializable> JmsCallable<E, R> createCallable(
		final Destination destination, final Class<R> resultClazz) {
	    return JmsClients.createCallable(context, destination, resultClazz);
	}

	@Override
	public <E extends Serializable, R extends Serializable> JmsCallable<E, R> createCallableQueue(
		final String queuePhysicalName, final Class<R> resultClazz) {
	    return JmsClients.createCallableQueue(context, queuePhysicalName, resultClazz);
	}

	@Override
	public <E extends Serializable, R extends Serializable> JmsCallable<E, R> createCallableTopic(
		final String topicPhysicalName, final Class<R> resultClazz) {
	    return JmsClients.createCallableTopic(context, topicPhysicalName, resultClazz);
	}

    }
}
