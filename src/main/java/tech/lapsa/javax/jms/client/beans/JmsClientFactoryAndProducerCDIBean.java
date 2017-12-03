package tech.lapsa.javax.jms.client.beans;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.jms.Destination;

import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.naming.MyNaming;
import tech.lapsa.javax.cdi.commons.MyAnnotated;
import tech.lapsa.javax.jms.client.JmsCallableClient;
import tech.lapsa.javax.jms.client.JmsClientFactory;
import tech.lapsa.javax.jms.client.JmsConsumerClient;
import tech.lapsa.javax.jms.client.JmsDestination;
import tech.lapsa.javax.jms.client.JmsEventNotificatorClient;
import tech.lapsa.javax.jms.client.JmsResultType;
import tech.lapsa.javax.jms.service.ejbBeans.JmsInternalClient;

@Dependent
public class JmsClientFactoryAndProducerCDIBean implements JmsClientFactory {

    @Inject
    private JmsInternalClient internalClient;

    @Override
    public <T extends Serializable> JmsEventNotificatorClient<T> createEventNotificator(Destination destination) {
	return new JmsEventNotificatorImpl<>(internalClient, destination);
    }

    @Override
    public <T extends Serializable> JmsConsumerClient<T> createConsumer(Destination destination) {
	return new JmsConsumerImpl<>(internalClient, destination);
    }

    @Override
    public <T extends Serializable, R extends Serializable> JmsCallableClient<T, R> createCallable(
	    final Class<R> resultClazz, Destination destination) {
	return new JmsCallableImpl<>(resultClazz, internalClient, destination);
    }

    @SuppressWarnings("unchecked")
    @Produces
    public <T extends Serializable, R extends Serializable> JmsCallableClient<T, R> produceCalable(
	    final InjectionPoint ip) {
	final Destination destination = MyNaming.requireResource(ip.getAnnotated() //
		.getAnnotation(JmsDestination.class) //
		.value(), Destination.class);
	final Class<? extends Serializable> wildcardResultClazz //
		= MyAnnotated.requireAnnotation(ip.getAnnotated(), JmsResultType.class) //
			.value();
	final Class<R> resultClazz;
	try {
	    resultClazz = (Class<R>) wildcardResultClazz;
	} catch (ClassCastException e) {
	    throw MyExceptions.illegalStateFormat("Types not safe");
	}

	return createCallable(resultClazz, destination);
    }

    @Produces
    public <T extends Serializable> JmsConsumerClient<T> produceConsumer(final InjectionPoint ip) {
	final Destination destination = MyNaming.requireResource(ip.getAnnotated() //
		.getAnnotation(JmsDestination.class) //
		.value(), Destination.class);
	return createConsumer(destination);
    }

    @Produces
    public <T extends Serializable> JmsEventNotificatorClient<T> produceEventNotificator(final InjectionPoint ip) {
	final Destination destination = MyNaming.requireResource(ip.getAnnotated() //
		.getAnnotation(JmsDestination.class) //
		.value(), Destination.class);
	return createEventNotificator(destination);
    }
}
