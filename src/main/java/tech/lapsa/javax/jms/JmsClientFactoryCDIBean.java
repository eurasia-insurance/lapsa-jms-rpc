package tech.lapsa.javax.jms;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;

import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.naming.MyNaming;
import tech.lapsa.javax.cdi.utility.MyAnnotated;

@Dependent
public class JmsClientFactoryCDIBean implements JmsClientFactory {

    @SuppressWarnings("unchecked")
    @Produces
    public <T extends Serializable, R extends Serializable> JmsCallable<T, R> produceCalable(final InjectionPoint ip) {
	final Destination destination = MyNaming.requireResource(ip.getAnnotated() //
		.getAnnotation(JmsDestinationMappedName.class) //
		.value(), Destination.class);
	@SuppressWarnings("unused")
	final Class<? extends Serializable> entityClazz //
		= MyAnnotated.requireAnnotation(ip.getAnnotated(), JmsServiceEntityType.class) //
			.value();
	final Class<? extends Serializable> wildcartResultClazz //
		= MyAnnotated.requireAnnotation(ip.getAnnotated(), JmsCallableResultType.class) //
			.value();

	final Class<R> resultClazz;
	try {
	    resultClazz = (Class<R>) wildcartResultClazz;
	} catch (ClassCastException e) {
	    throw MyExceptions.illegalStateFormat("Types not safe");
	}

	return JmsClients.createCallable(context, destination, resultClazz);
    }

    @Produces
    public <T extends Serializable> JmsConsumer<T> produceConsumer(final InjectionPoint ip) {
	final Destination destination = MyNaming.requireResource(ip.getAnnotated() //
		.getAnnotation(JmsDestinationMappedName.class) //
		.value(), Destination.class);
	@SuppressWarnings("unused")
	final Class<? extends Serializable> entityClazz //
		= MyAnnotated.requireAnnotation(ip.getAnnotated(), JmsServiceEntityType.class) //
			.value();
	return JmsClients.createConsumer(context, destination);
    }

    @Produces
    public <T extends Serializable> JmsSender<T> createSender(final InjectionPoint ip) {
	final Destination destination = MyNaming.requireResource(ip.getAnnotated() //
		.getAnnotation(JmsDestinationMappedName.class) //
		.value(), Destination.class);
	@SuppressWarnings("unused")
	final Class<? extends Serializable> entityClazz //
		= MyAnnotated.requireAnnotation(ip.getAnnotated(), JmsServiceEntityType.class) //
			.value();
	return JmsClients.createSender(context, destination);
    }

    @Inject
    private JMSContext context;

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