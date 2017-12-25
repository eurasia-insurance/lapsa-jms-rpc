package tech.lapsa.javax.jms.client;

import java.io.Serializable;

import javax.jms.Destination;

public interface JmsClientFactory {

    <T extends Serializable> JmsEventNotificatorClient<T> createEventNotificator(Destination destination);

    <T extends Serializable> JmsConsumerClient<T> createConsumer(Destination destination);

    <T extends Serializable, R extends Serializable> JmsCallableClient<T, R> createCallable(Class<R> resultClazz,
	    Destination destination);

}