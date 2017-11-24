package tech.lapsa.javax.jms;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jms.Destination;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;

@Singleton
public class JmsFactoryProducerCDIBean {

    @Inject
    @JMSConnectionFactory(Constants.JNDI_DEFAULT_JMS_CONNECTION_FACTORY)
    private JMSContext context;

    private JmsClientFactory clientFactoryInstance;

    @PostConstruct
    public void init() {
	clientFactoryInstance = new JmsClientFactoryImpl();
    }

    @Produces
    public JmsClientFactory getClientFactory() {
	return clientFactoryInstance;
    }

    private class JmsClientFactoryImpl implements JmsClientFactory {

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
	public <E extends Serializable> JmsSender<E> createSender(final Destination destination) {
	    return JmsClients.createSender(context, destination);
	}

	@Override
	public <E extends Serializable> JmsSender<E> createSenderQueue(final String queuePhysicalName) {
	    return JmsClients.createSenderQueue(context, queuePhysicalName);
	}

	@Override
	public <E extends Serializable> JmsSender<E> createSenderTopic(final String topicPhysicalName) {
	    return JmsClients.createSenderTopic(context, topicPhysicalName);
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
