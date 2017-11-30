package tech.lapsa.javax.jms.internal;

import java.io.Serializable;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.Topic;

import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.logging.MyLogger;
import tech.lapsa.java.commons.logging.MyLogger.MyLevel;
import tech.lapsa.javax.jms.ResponseNotReceivedException;

@Stateless
public class JmsInternalClientBean implements JmsInternalClient {

    @Inject
    private JMSContext context;

    private final MyLogger logger = MyLogger.newBuilder() //
	    .withNameOf(JmsInternalClient.class) //
	    .addInstantPrefix() //
	    .addPrefix("JMS-Client") //
	    .build();

    private final MyLevel debugLevel = logger.DEBUG;
    private final MyLevel traceLevel = logger.TRACE;
    private final MyLevel superTraceLevel = logger.SUPER_TRACE;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendWithReplyTo(Destination destination, Message message) {
	try {
	    final String corellationID = UUID.randomUUID().toString();
	    final JMSProducer producer = context.createProducer();
	    final Topic replyToDestination = context.createTopic(this.getClass().getName() + ".replyTo");
	    message.setJMSReplyTo(replyToDestination);
	    message.setJMSCorrelationID(corellationID);
	    producer.send(destination, message);
	    debugLevel.log("JMS-Message was sent to %1$s", MyMessages.getNameOf(destination));
	    superTraceLevel.log("... with JMSMessageID %1$s", MyMessages.getJMSMessageIDOf(message));
	    traceLevel.log("... with JMSCorrellationID %1$s", MyMessages.getJMSCorellationIDOf(message));
	    traceLevel.log("... with JMSReplyTo %1$s", MyMessages.getNameOf(replyToDestination));
	} catch (JMSException e) {
	    throw MyMessages.uchedked(e);
	}
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void send(final Destination destination, final Message... messages) {
	final JMSProducer producer = context.createProducer();
	for (Message message : messages) {
	    producer.send(destination, message);
	    debugLevel.log("JMS-Message was sent to %1$s", MyMessages.getNameOf(destination));
	    superTraceLevel.log("... with JMSMessageID %1$s", MyMessages.getJMSMessageIDOf(message));
	}
    }

    @Override
    public Message receiveReplyOn(Message message, long timeout) throws ResponseNotReceivedException {

	final Destination replyToDestination;
	try {
	    replyToDestination = message.getJMSReplyTo();
	} catch (JMSException e) {
	    throw MyMessages.uchedked(e);
	}
	if (replyToDestination == null)
	    throw MyExceptions.illegalArgumentFormat(
		    "JMS-Message %1$ is not configured as ReplyTo message. JMSReplyTo property is null",
		    MyMessages.getJMSMessageIDOf(message));
	debugLevel.log("JMS-Reply started receiving from %1$s", MyMessages.getNameOf(replyToDestination));
	final Optional<String> corellationID = MyMessages.optJMSCorellationIDOf(message);
	if (!corellationID.isPresent())
	    throw MyExceptions.illegalArgumentFormat(
		    "JMS-Message %1$ has no JMSMessageID property. This may be due to it's not sent.", message);
	final String messageSelector = String.format("JMSCorrelationID = '%1$s'", corellationID.get());
	try (final JMSConsumer consumer = context.createConsumer(replyToDestination, messageSelector)) {
	    traceLevel.log("... with timeout of %1$s ms", timeout);
	    traceLevel.log("... with message selector \"%1$s\"", messageSelector);
	    final Message reply = consumer.receive(timeout);
	    if (reply != null) {
		debugLevel.log("JMS-Reply received from %1$s", MyMessages.getJMSDestination(reply));
		superTraceLevel.log("... with JMSMessageID %1$s", MyMessages.getJMSMessageIDOf(reply));
		traceLevel.log("... with JMSCorrellationID %1$s", MyMessages.getJMSCorellationIDOf(reply));
		return reply;
	    } else
		debugLevel.log("JMS-Reply NOT received in %1$s ms.", timeout);

	    throw MyExceptions.runtimeExceptionFormat(ResponseNotReceivedException::new,
		    "Error receiving JMS-Reply on message '%1$s' from %2$s with %3$s",
		    message, // 1
		    MyMessages.getNameOf(replyToDestination), // 2
		    messageSelector // 3
	    );
	} finally {
	    if (replyToDestination instanceof TemporaryQueue) {
		try {
		    debugLevel.log("Dropping temporary queue %1$s", MyMessages.getNameOf(replyToDestination));
		    ((TemporaryQueue) replyToDestination).delete();
		    traceLevel.log("... dropped");
		} catch (JMSException ignored) {
		    traceLevel.log("... not dropped with exception %1$s", ignored.getMessage());
		}
	    }
	    if (replyToDestination instanceof TemporaryTopic) {
		try {
		    debugLevel.log("Dropping temporary topic %1$s", MyMessages.getNameOf(replyToDestination));
		    ((TemporaryTopic) replyToDestination).delete();
		    traceLevel.log("... dropped.");
		} catch (JMSException ignored) {
		    traceLevel.log("... not dropped with exception %1$s", ignored.getMessage());
		}
	    }
	}
    }

    @Override
    public Message createMessage(final Serializable entity) {
	return context.createObjectMessage(entity);
    }

    @Override
    public Message createMessage(final Serializable entity, final Properties properties) {
	final Message message = context.createObjectMessage(entity);
	if (properties != null)
	    MyMessages.propertiesToMessage(message, properties);
	return message;
    }

    @Override
    public Queue createQueue(final String queuePhysicalName) {
	return context.createQueue(queuePhysicalName);
    }

    @Override
    public Topic createTopic(final String topicPhysicalName) {
	return context.createTopic(topicPhysicalName);
    }
}
