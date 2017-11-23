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
public class MyJMSClientProducerBean {

    @Inject
    @JMSConnectionFactory(Constants.JNDI_DEFAULT_JMS_CONNECTION_FACTORY)
    private JMSContext context;

    private MyJMSClient instance;

    @PostConstruct
    public void init() {
	instance = new MyJMSClientImpl();
    }

    @Produces
    public MyJMSClient generateClient() {
	return instance;
    }

    private class MyJMSClientImpl implements MyJMSClient {

	@Override
	public <T extends Serializable> MyJMSConsumer<T> createConsumer(final Destination destination) {
	    return MyJMSFunctions.createConsumer(context, destination);
	}

	@Override
	public <T extends Serializable> MyJMSConsumer<T> createQueueConsumer(final String queuePhysicalName) {
	    return MyJMSFunctions.createQueueConsumer(context, queuePhysicalName);
	}

	@Override
	public <T extends Serializable> MyJMSConsumer<T> createTopicConsumer(final String topicPhysicalName) {
	    return MyJMSFunctions.createTopicConsumer(context, topicPhysicalName);
	}

	//

	@Override
	public <T extends Serializable> MyJMSMultipleConsumer<T> createMultipleConsumer(final Destination destination)
		throws JMSException {
	    return MyJMSFunctions.createMultipleConsumer(context, destination);
	}

	@Override
	public <T extends Serializable> MyJMSMultipleConsumer<T> createMultipleQueueConsumer(
		final String queuePhysicalName)
		throws JMSException {
	    return MyJMSFunctions.createMultipleQueueConsumer(context, queuePhysicalName);
	}

	@Override
	public <T extends Serializable> MyJMSMultipleConsumer<T> createMultipleTopicConsumer(
		final String topicPhysicalName)
		throws JMSException {
	    return MyJMSFunctions.createMultipleTopicConsumer(context, topicPhysicalName);
	}

	//

	@Override
	public <T extends Serializable, R extends Serializable> MyJMSFunction<T, R> createFunction(
		final Destination destination, final Class<R> outClazz) {
	    return MyJMSFunctions.createFunction(context, destination, outClazz);
	}

	@Override
	public <T extends Serializable, R extends Serializable> MyJMSFunction<T, R> createQueueFunction(
		final String queuePhysicalName, final Class<R> outClazz) {
	    return MyJMSFunctions.createQueueFunction(context, queuePhysicalName, outClazz);
	}

	@Override
	public <T extends Serializable, R extends Serializable> MyJMSFunction<T, R> createTopicFunction(
		final String topicPhysicalName, final Class<R> outClazz) {
	    return MyJMSFunctions.createTopicFunction(context, topicPhysicalName, outClazz);
	}

    }
}
