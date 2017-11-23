package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

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

    public static final class VoidResult implements Serializable {
	private static final long serialVersionUID = 1L;
    }

    //

    public static <T extends Serializable> MyJMSConsumer<T> createConsumer(final JMSContext context,
	    final Destination destination) {
	return new MyJMSConsumerImpl<>(context, destination);
    }

    public static <T extends Serializable> MyJMSConsumer<T> createQueueConsumer(final JMSContext context,
	    final String queuePhysicalName) {
	return new MyJMSConsumerImpl<>(context, context.createQueue(queuePhysicalName));
    }

    public static <T extends Serializable> MyJMSConsumer<T> createTopicConsumer(final JMSContext context,
	    final String topicPhysicalName) {
	return new MyJMSConsumerImpl<>(context, context.createTopic(topicPhysicalName));
    }

    //

    public static <T extends Serializable> MyJMSMultipleConsumer<T> createMultipleConsumer(
	    final JMSContext context,
	    final Destination destination) throws JMSException {
	return new MyJMSMultipleConsumerImpl<>(context, destination);
    }

    public static <T extends Serializable> MyJMSMultipleConsumer<T> createMultipleQueueConsumer(
	    final JMSContext context,
	    final String queuePhysicalName) throws JMSException {
	return new MyJMSMultipleConsumerImpl<>(context, context.createQueue(queuePhysicalName));
    }

    public static <T extends Serializable> MyJMSMultipleConsumer<T> createMultipleTopicConsumer(
	    final JMSContext context,
	    final String topicPhysicalName) throws JMSException {
	return new MyJMSMultipleConsumerImpl<>(context, context.createTopic(topicPhysicalName));
    }

    //

    public static <T extends Serializable, R extends Serializable> MyJMSFunction<T, R> createFunction(
	    final JMSContext context, final Destination destination, Class<R> outClazz) {
	return new MyJMSFunctionImpl<>(outClazz, context, destination);
    }

    public static <T extends Serializable, R extends Serializable> MyJMSFunction<T, R> createQueueFunction(
	    final JMSContext context, final String queuePhysicalName, Class<R> outClazz) {
	return new MyJMSFunctionImpl<>(outClazz, context, context.createQueue(queuePhysicalName));
    }

    public static <T extends Serializable, R extends Serializable> MyJMSFunction<T, R> createTopicFunction(
	    final JMSContext context, final String topicPhysicalName, Class<R> outClazz) {
	return new MyJMSFunctionImpl<>(outClazz, context, context.createTopic(topicPhysicalName));
    }

    //

    static class Base<OUT extends Serializable, IN extends Serializable> {

	private static final int DEFAULT_TIMEOUT = 10000; // 10 seconds

	final JMSContext context;
	final Destination destination;
	final Class<IN> inC;

	private Base(final Class<IN> inC, final JMSContext context,
		final Destination destination) {
	    this.inC = MyObjects.requireNonNull(inC, "inC");
	    this.context = MyObjects.requireNonNull(context, "context");
	    this.destination = MyObjects.requireNonNull(destination, "destination");
	}

	@SafeVarargs
	final void _send(final OUT... outOs) throws JMSException {
	    _send(null, outOs);
	}

	@SafeVarargs
	final void _send(final Properties properties, final OUT... outOs) throws JMSException {
	    _send(context, destination, properties, outOs);
	}

	@SafeVarargs
	final static <OUT extends Serializable> void _send(final JMSContext context, final Destination destination,
		final Properties properties, final OUT... outOs) throws JMSException {
	    final JMSProducer producer = context.createProducer();
	    if (properties != null)
		MyMessages.propertiesToJMSProducer(producer, properties);
	    for (OUT outO : outOs)
		producer.send(destination, outO);
	}

	final IN _request(final OUT outO)
		throws JMSException, ResponseNotReceivedException, InvalidResponseTypeException {
	    return _request(outO, null);
	}

	final IN _request(final OUT outO, final Properties properties)
		throws JMSException, ResponseNotReceivedException, InvalidResponseTypeException {

	    Message inM = null;

	    {
		TemporaryQueue replyToD = null;
		try {
		    final String jmsCorellationID = UUID.randomUUID().toString();

		    replyToD = context.createTemporaryQueue();
		    JMSProducer producer = context.createProducer() //
			    .setJMSReplyTo(replyToD) //
			    .setJMSCorrelationID(jmsCorellationID);

		    if (properties != null)
			MyMessages.propertiesToJMSProducer(producer, properties);
		    producer.send(destination, outO);

		    final String messageSelector = String.format("JMSCorrelationID = '%1$s'", jmsCorellationID);
		    try (final JMSConsumer consumer = context.createConsumer(replyToD, messageSelector)) {
			inM = consumer.receive(DEFAULT_TIMEOUT);
		    }

		} finally {
		    try {
			if (replyToD != null)
			    replyToD.delete();
		    } catch (JMSException ignored) {
		    }
		}
	    }

	    if (inM == null)
		throw new ResponseNotReceivedException();

	    try {
		if (inM.isBodyAssignableTo(inC))
		    return inM.getBody(inC);

		if (inM.isBodyAssignableTo(ValidationException.class))
		    throw inM.getBody(ValidationException.class);

		if (inM.isBodyAssignableTo(RuntimeException.class))
		    throw inM.getBody(RuntimeException.class);

		if (inM.isBodyAssignableTo(Serializable.class)) {
		    final Serializable inO = inM.getBody(Serializable.class);
		    throw new InvalidResponseTypeException(inC, inO.getClass());
		}

		throw new InvalidResponseTypeException("Unknown response type");

	    } catch (MessageFormatException e) {
		throw inM.getBody(RuntimeException.class);
	    }

	}
    }

    public static class ResponseNotReceivedException extends Exception {
	private static final long serialVersionUID = 1L;
    }

    public static class InvalidResponseTypeException extends Exception {
	private static final long serialVersionUID = 1L;

	InvalidResponseTypeException(Class<?> expected, Class<?> actual) {
	    super(String.format("Expected type is %1$s but the actual type was %2$s", expected, actual));
	}

	InvalidResponseTypeException(String message) {
	    super(message);
	}
    }

    static final class MyJMSMultipleConsumerImpl<IN extends Serializable> extends Base<IN, VoidResult>
	    implements MyJMSMultipleConsumer<IN> {

	private MyJMSMultipleConsumerImpl(final JMSContext context, final Destination destination)
		throws JMSException {
	    super(VoidResult.class, context, destination);
	}

	@Override
	public void close() throws JMSException, IllegalStateException {
	}

	@Override
	@SafeVarargs
	public final void acceptNoWait(IN... inOs) throws JMSException {
	    _send(context, destination, null, inOs);
	}

    }

    static final class MyJMSConsumerImpl<IN extends Serializable> extends Base<IN, VoidResult>
	    implements MyJMSConsumer<IN> {

	private MyJMSConsumerImpl(final JMSContext context, final Destination destination) {
	    super(VoidResult.class, context, destination);
	}

	@Override
	public void accept(final IN inO, final Properties properties)
		throws JMSException, ResponseNotReceivedException, InvalidResponseTypeException {
	    VoidResult outO = _request(inO, properties);
	    if (outO == null)
		throw new RuntimeException(VoidResult.class.getName() + " expected");
	}

	@Override
	public void accept(final IN inO)
		throws JMSException, ResponseNotReceivedException, InvalidResponseTypeException {
	    accept(inO, null);
	}

	@Override
	public void acceptNoWait(final IN inO, Properties properties) throws JMSException {
	    _send(properties, inO);
	}

	@Override
	public void acceptNoWait(final IN inO) throws JMSException {
	    acceptNoWait(inO, null);
	}

    }

    static final class MyJMSFunctionImpl<IN extends Serializable, OUT extends Serializable> extends Base<IN, OUT>
	    implements MyJMSFunction<IN, OUT> {

	private MyJMSFunctionImpl(final Class<OUT> outC, final JMSContext context, final Destination destination) {
	    super(outC, context, destination);
	}

	@Override
	public OUT apply(final IN inO, final Properties properties)
		throws JMSException, ResponseNotReceivedException, InvalidResponseTypeException {
	    return _request(inO, properties);
	}

	@Override
	public OUT apply(final IN inO)
		throws JMSException, ResponseNotReceivedException, InvalidResponseTypeException {
	    return apply(inO, null);
	}

    }
}
