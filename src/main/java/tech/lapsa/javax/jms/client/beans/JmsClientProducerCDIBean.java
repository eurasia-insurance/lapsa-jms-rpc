package tech.lapsa.javax.jms;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.jms.Destination;

import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.naming.MyNaming;
import tech.lapsa.javax.cdi.utility.MyAnnotated;
import tech.lapsa.javax.jms.internal.JmsInternalClient;

@Dependent
public class JmsClientFactoryCDIBean implements JmsClientFactory {

    @Inject
    private JmsInternalClient client;

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

	return JmsClients.createCallable(client, destination, resultClazz);
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
	return JmsClients.createConsumer(client, destination);
    }

    @Produces
    public <T extends Serializable> JmsEventNotificator<T> produceEventNotificator(final InjectionPoint ip) {
	final Destination destination = MyNaming.requireResource(ip.getAnnotated() //
		.getAnnotation(JmsDestinationMappedName.class) //
		.value(), Destination.class);
	@SuppressWarnings("unused")
	final Class<? extends Serializable> entityClazz //
		= MyAnnotated.requireAnnotation(ip.getAnnotated(), JmsServiceEntityType.class) //
			.value();
	return JmsClients.createSender(client, destination);
    }

    @Override
    public <E extends Serializable> JmsConsumer<E> createConsumer(final Destination destination) {
	return JmsClients.createConsumer(client, destination);
    }

    @Override
    public <E extends Serializable> JmsConsumer<E> createConsumerQueue(final String queuePhysicalName) {
	return JmsClients.createConsumerQueue(client, queuePhysicalName);
    }

    @Override
    public <E extends Serializable> JmsConsumer<E> createConsumerTopic(final String topicPhysicalName) {
	return JmsClients.createConsumerTopic(client, topicPhysicalName);
    }

    //

    @Override
    public <E extends Serializable> JmsEventNotificator<E> createEventNotificator(final Destination destination) {
	return JmsClients.createSender(client, destination);
    }

    @Override
    public <E extends Serializable> JmsEventNotificator<E> createEventNotificatorQueue(final String queuePhysicalName) {
	return JmsClients.createSenderQueue(client, queuePhysicalName);
    }

    @Override
    public <E extends Serializable> JmsEventNotificator<E> createEventNotificatorTopic(final String topicPhysicalName) {
	return JmsClients.createSenderTopic(client, topicPhysicalName);
    }

    //

    @Override
    public <E extends Serializable, R extends Serializable> JmsCallable<E, R> createCallable(
	    final Destination destination, final Class<R> resultClazz) {
	return JmsClients.createCallable(client, destination, resultClazz);
    }

    @Override
    public <E extends Serializable, R extends Serializable> JmsCallable<E, R> createCallableQueue(
	    final String queuePhysicalName, final Class<R> resultClazz) {
	return JmsClients.createCallableQueue(client, queuePhysicalName, resultClazz);
    }

    @Override
    public <E extends Serializable, R extends Serializable> JmsCallable<E, R> createCallableTopic(
	    final String topicPhysicalName, final Class<R> resultClazz) {
	return JmsClients.createCallableTopic(client, topicPhysicalName, resultClazz);
    }
}