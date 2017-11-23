package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;

import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageFormatException;
import javax.jms.TemporaryQueue;
import javax.validation.ValidationException;

import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.javax.jms.MyJMSClient.MyJMSConsumer;
import tech.lapsa.javax.jms.MyJMSClient.MyJMSFunction;
import tech.lapsa.javax.jms.MyJMSClient.MyJMSMultipleConsumer;

public final class MyJMSFunctions {

    private MyJMSFunctions() {
    }

    //

    public static <E extends Serializable> MyJMSConsumer<E> createConsumer(final JMSContext context,
	    final Destination destination) {
	return new MyJMSConsumerImpl<>(context, destination);
    }

    public static <E extends Serializable> MyJMSConsumer<E> createQueueConsumer(final JMSContext context,
	    final String queuePhysicalName) {
	return new MyJMSConsumerImpl<>(context, context.createQueue(queuePhysicalName));
    }

    public static <E extends Serializable> MyJMSConsumer<E> createTopicConsumer(final JMSContext context,
	    final String topicPhysicalName) {
	return new MyJMSConsumerImpl<>(context, context.createTopic(topicPhysicalName));
    }

    //

    public static <E extends Serializable> MyJMSMultipleConsumer<E> createMultipleConsumer(
	    final JMSContext context,
	    final Destination destination) throws JMSException {
	return new MyJMSMultipleConsumerImpl<>(context, destination);
    }

    public static <E extends Serializable> MyJMSMultipleConsumer<E> createMultipleQueueConsumer(
	    final JMSContext context,
	    final String queuePhysicalName) throws JMSException {
	return new MyJMSMultipleConsumerImpl<>(context, context.createQueue(queuePhysicalName));
    }

    public static <E extends Serializable> MyJMSMultipleConsumer<E> createMultipleTopicConsumer(
	    final JMSContext context,
	    final String topicPhysicalName) throws JMSException {
	return new MyJMSMultipleConsumerImpl<>(context, context.createTopic(topicPhysicalName));
    }

    //

    public static <E extends Serializable, R extends Serializable> MyJMSFunction<E, R> createFunction(
	    final JMSContext context, final Destination destination, final Class<R> resultClazz) {
	return new MyJMSFunctionImpl<>(resultClazz, context, destination);
    }

    public static <E extends Serializable, R extends Serializable> MyJMSFunction<E, R> createQueueFunction(
	    final JMSContext context, final String queuePhysicalName, final Class<R> resultClazz) {
	return new MyJMSFunctionImpl<>(resultClazz, context, context.createQueue(queuePhysicalName));
    }

    public static <E extends Serializable, R extends Serializable> MyJMSFunction<E, R> createTopicFunction(
	    final JMSContext context, final String topicPhysicalName, final Class<R> resultClazz) {
	return new MyJMSFunctionImpl<>(resultClazz, context, context.createTopic(topicPhysicalName));
    }

    //

    static class Base<E extends Serializable, R extends Serializable> {

	private static final int DEFAULT_TIMEOUT = 20 * 1000; // 20 seconds

	final JMSContext context;
	final Destination destination;
	final Class<R> resultClazz;

	private Base(final Class<R> resultClazz, final JMSContext context,
		final Destination destination) {
	    this.resultClazz = MyObjects.requireNonNull(resultClazz, "resultClazz");
	    this.context = MyObjects.requireNonNull(context, "context");
	    this.destination = MyObjects.requireNonNull(destination, "destination");
	}

	@SafeVarargs
	final void _send(final E... entities) throws JMSException {
	    _send(null, entities);
	}

	@SafeVarargs
	final void _send(final Properties properties, final E... entities) throws JMSException {
	    _send(context, destination, properties, entities);
	}

	@SafeVarargs
	final static <E extends Serializable> void _send(final JMSContext context, final Destination destination,
		final Properties properties, final E... entities) throws JMSException {
	    final JMSProducer producer = context.createProducer();
	    if (properties != null)
		MyMessages.propertiesToJMSProducer(producer, properties);
	    for (final E entity : entities)
		producer.send(destination, entity);
	}

	final R _request(final E entity)
		throws JMSException, ResponseNotReceivedException, InvalidResponseTypeException {
	    return _request(entity, null);
	}

	final R _request(final E entity, final Properties properties)
		throws JMSException, ResponseNotReceivedException, InvalidResponseTypeException {

	    Message resultM = null;

	    {
		TemporaryQueue replyToD = null;
		try {
		    replyToD = context.createTemporaryQueue();
		    final Message entityM = context.createObjectMessage(entity);
		    entityM.setJMSReplyTo(replyToD);
		    if (properties != null)
			MyMessages.propertiesToMessage(entityM, properties);
		    context.createProducer().send(destination, entityM);
		    final String jmsCorellationID = entityM.getJMSMessageID();
		    final String messageSelector = String.format("JMSCorrelationID = '%1$s'", jmsCorellationID);
		    try (final JMSConsumer consumer = context.createConsumer(replyToD, messageSelector)) {
			resultM = consumer.receive(DEFAULT_TIMEOUT);
		    }

		} finally {
		    try {
			if (replyToD != null)
			    replyToD.delete();
		    } catch (final JMSException ignored) {
		    }
		}
	    }

	    if (resultM == null)
		throw new ResponseNotReceivedException();

	    try {
		if (resultM.isBodyAssignableTo(resultClazz))
		    return resultM.getBody(resultClazz);

		if (resultM.isBodyAssignableTo(ValidationException.class))
		    throw resultM.getBody(ValidationException.class);

		if (resultM.isBodyAssignableTo(RuntimeException.class))
		    throw resultM.getBody(RuntimeException.class);

		if (resultM.isBodyAssignableTo(Serializable.class)) {
		    final Serializable inO = resultM.getBody(Serializable.class);
		    throw new InvalidResponseTypeException(resultClazz, inO.getClass());
		}

		throw new InvalidResponseTypeException("Unknown response type");

	    } catch (final MessageFormatException e) {
		throw resultM.getBody(RuntimeException.class);
	    }
	}
    }

    static final class MyJMSMultipleConsumerImpl<E extends Serializable> extends Base<E, VoidResult>
	    implements MyJMSMultipleConsumer<E> {

	private MyJMSMultipleConsumerImpl(final JMSContext context, final Destination destination)
		throws JMSException {
	    super(VoidResult.class, context, destination);
	}

	@Override
	public void close() throws JMSException, IllegalStateException {
	}

	@Override
	@SafeVarargs
	public final void acceptNoWait(final E... entities) throws JMSException {
	    _send(context, destination, null, entities);
	}
    }

    static final class MyJMSConsumerImpl<E extends Serializable> extends Base<E, VoidResult>
	    implements MyJMSConsumer<E> {

	private MyJMSConsumerImpl(final JMSContext context, final Destination destination) {
	    super(VoidResult.class, context, destination);
	}

	@Override
	public void accept(final E entity, final Properties properties)
		throws JMSException, ResponseNotReceivedException, InvalidResponseTypeException {
	    final VoidResult outO = _request(entity, properties);
	    if (outO == null)
		throw new RuntimeException(VoidResult.class.getName() + " expected");
	}

	@Override
	public void accept(final E entity)
		throws JMSException, ResponseNotReceivedException, InvalidResponseTypeException {
	    accept(entity, null);
	}

	@Override
	public void acceptNoWait(final E entity, final Properties properties) throws JMSException {
	    _send(properties, entity);
	}

	@Override
	public void acceptNoWait(final E entity) throws JMSException {
	    acceptNoWait(entity, null);
	}
    }

    static final class MyJMSFunctionImpl<E extends Serializable, R extends Serializable> extends Base<E, R>
	    implements MyJMSFunction<E, R> {

	private MyJMSFunctionImpl(final Class<R> resultClazz, final JMSContext context, final Destination destination) {
	    super(resultClazz, context, destination);
	}

	@Override
	public R apply(final E entity, final Properties properties)
		throws JMSException, ResponseNotReceivedException, InvalidResponseTypeException {
	    return _request(entity, properties);
	}

	@Override
	public R apply(final E entity)
		throws JMSException, ResponseNotReceivedException, InvalidResponseTypeException {
	    return apply(entity, null);
	}
    }
}
