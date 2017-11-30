package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyStreams;
import tech.lapsa.javax.jms.ConsumerServiceDrivenBean.VoidResult;
import tech.lapsa.javax.jms.JmsClientFactory.JmsCallable;
import tech.lapsa.javax.jms.JmsClientFactory.JmsConsumer;
import tech.lapsa.javax.jms.JmsClientFactory.JmsEventNotificator;
import tech.lapsa.javax.jms.internal.JmsInternalClient;
import tech.lapsa.javax.jms.internal.MyMessages;

public final class JmsClients {

    private JmsClients() {
    }

    //

    public static <E extends Serializable> JmsConsumer<E> createConsumer(final JmsInternalClient client,
	    final Destination destination) {
	return new ConsumerClientImpl<>(client, destination);
    }

    public static <E extends Serializable> JmsConsumer<E> createConsumerQueue(final JmsInternalClient client,
	    final String queuePhysicalName) {
	return new ConsumerClientImpl<>(client, client.createQueue(queuePhysicalName));
    }

    public static <E extends Serializable> JmsConsumer<E> createConsumerTopic(final JmsInternalClient client,
	    final String topicPhysicalName) {
	return new ConsumerClientImpl<>(client, client.createTopic(topicPhysicalName));
    }

    //

    public static <E extends Serializable> JmsEventNotificator<E> createSender(final JmsInternalClient client,
	    final Destination destination) {
	return new SenderClientImpl<>(client, destination);
    }

    public static <E extends Serializable> JmsEventNotificator<E> createSenderQueue(final JmsInternalClient client,
	    final String queuePhysicalName) {
	return new SenderClientImpl<>(client, client.createQueue(queuePhysicalName));
    }

    public static <E extends Serializable> JmsEventNotificator<E> createSenderTopic(final JmsInternalClient client,
	    final String topicPhysicalName) {
	return new SenderClientImpl<>(client, client.createTopic(topicPhysicalName));
    }

    //

    public static <E extends Serializable, R extends Serializable> JmsCallable<E, R> createCallable(
	    final JmsInternalClient client, final Destination destination, final Class<R> resultClazz) {
	return new CallableClientImpl<>(resultClazz, client, destination);
    }

    public static <E extends Serializable, R extends Serializable> JmsCallable<E, R> createCallableQueue(
	    final JmsInternalClient client, final String queuePhysicalName, final Class<R> resultClazz) {
	return new CallableClientImpl<>(resultClazz, client, client.createQueue(queuePhysicalName));
    }

    public static <E extends Serializable, R extends Serializable> JmsCallable<E, R> createCallableTopic(
	    final JmsInternalClient client, final String topicPhysicalName, final Class<R> resultClazz) {
	return new CallableClientImpl<>(resultClazz, client, client.createTopic(topicPhysicalName));
    }

    //

    private static class BaseClient<E extends Serializable, R extends Serializable> {

	private static final int DEFAULT_TIMEOUT = 20 * 1000; // 20 seconds

	final Destination destination;
	final Class<R> resultClazz;
	final JmsInternalClient client;

	private BaseClient(final Class<R> resultClazz, final JmsInternalClient client,
		final Destination destination) {
	    this.resultClazz = MyObjects.requireNonNull(resultClazz, "resultClazz");
	    this.destination = MyObjects.requireNonNull(destination, "destination");
	    this.client = MyObjects.requireNonNull(client, "client");
	}

	@SafeVarargs
	final void _sendNoWait(final E... entities) {
	    _sendNoWait(null, entities);
	}

	@SafeVarargs
	final void _sendNoWait(final Properties properties, final E... entities) {
	    try {
		Message[] messages = MyStreams.orEmptyOf(entities) //
			.map(e -> client.createMessage(e, properties)) //
			.toArray(Message[]::new);
		client.send(destination, messages);
	    } catch (JMSException e) {
		throw MyMessages.uchedked(e);
	    }
	}

	final R _send(final E entity) {
	    return _send(entity, null);
	}

	final R _send(final E entity, final Properties properties) {
	    try {

		final Message message = client.createMessage(entity, properties);
		client.sendWithReplyTo(destination, message);
		final Message reply = client.receiveReplyOn(message, DEFAULT_TIMEOUT);

		if (reply == null)
		    throw new ResponseNotReceivedException();

		if (reply.isBodyAssignableTo(resultClazz))
		    return reply.getBody(resultClazz);

		if (reply.isBodyAssignableTo(RuntimeException.class))
		    throw reply.getBody(RuntimeException.class);

		if (reply.isBodyAssignableTo(Serializable.class)) {
		    final Object wrongTypedObject = reply.getBody(Object.class);
		    if (wrongTypedObject != null)
			throw new UnexpectedResponseTypeException(resultClazz, wrongTypedObject.getClass());
		}

		throw new UnexpectedResponseTypeException("Unknown response type");

	    } catch (JMSException e) {
		throw MyMessages.uchedked(e);
	    }
	}
    }

    static final class SenderClientImpl<E extends Serializable> extends BaseClient<E, VoidResult>
	    implements JmsEventNotificator<E> {

	private SenderClientImpl(final JmsInternalClient client, final Destination destination) {
	    super(VoidResult.class, client, destination);

	}

	@Override
	@SafeVarargs
	public final void eventNotify(final E... entities) {
	    _sendNoWait(null, entities);
	}

	@Override
	public void eventNotify(E entity, Properties properties) {
	    _sendNoWait(properties, entity);
	}
    }

    static final class ConsumerClientImpl<E extends Serializable> extends BaseClient<E, VoidResult>
	    implements JmsConsumer<E> {

	private ConsumerClientImpl(final JmsInternalClient client, final Destination destination) {
	    super(VoidResult.class, client, destination);
	}

	@Override
	public void accept(final E entity, final Properties properties) {
	    final VoidResult outO = _send(entity, properties);
	    if (outO == null)
		throw new RuntimeException(VoidResult.class.getName() + " expected");
	}

	@Override
	public void accept(final E entity) {
	    accept(entity, null);
	}
    }

    static final class CallableClientImpl<E extends Serializable, R extends Serializable> extends BaseClient<E, R>
	    implements JmsCallable<E, R> {

	private CallableClientImpl(final Class<R> resultClazz, final JmsInternalClient client,
		final Destination destination) {
	    super(resultClazz, client, destination);
	}

	@Override
	public R call(final E entity, final Properties properties) {
	    return _send(entity, properties);
	}

	@Override
	public R call(final E entity) {
	    return call(entity, null);
	}
    }
}
