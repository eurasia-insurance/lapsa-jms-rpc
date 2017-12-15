package tech.lapsa.javax.jms.client.ejbBeans;

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
import tech.lapsa.javax.jms.Messages;
import tech.lapsa.javax.jms.client.ResponseNotReceivedException;
import tech.lapsa.javax.jms.commons.MyJMSs;

@Stateless
public class JmsInternalClientBean implements JmsInternalClient {

    @Inject
    private JMSContext context;

    private final MyLogger logger = MyLogger.newBuilder() //
	    .withNameOf(JmsInternalClient.class) //
	    .addInstantPrefix() //
	    .addPrefix("JMS-Client") //
	    .build();

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public UUID sendWithReplyTo(final Destination destination, final Message message) {
	try {
	    final UUID callId = UUID.randomUUID();
	    final String corellationID = callId.toString();
	    final JMSProducer producer = context.createProducer();
	    final Topic replyToDestination = context.createTopic(this.getClass().getName() + ".replyTo");
	    message.setJMSReplyTo(replyToDestination);
	    message.setJMSCorrelationID(corellationID);
	    producer.send(destination, message);
	    logger.DEBUG.log("%1$s JMS-Message was sent to %2$s", callId, MyJMSs.getNameOf(destination));
	    logger.SUPER_TRACE.log("%1$s ... with JMSMessageID %2$s", callId, MyJMSs.getJMSMessageIDOf(message));
	    logger.TRACE.log("%1$s ... with JMSCorrellationID %2$s", callId, MyJMSs.getJMSCorellationIDOf(message));
	    logger.TRACE.log("%1$s ... with JMSReplyTo %2$s", callId, MyJMSs.getNameOf(replyToDestination));
	    return callId;
	} catch (JMSException e) {
	    throw MyJMSs.uchedked(e);
	}
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void send(final Destination destination, final Message... messages) {
	try {
	    final JMSProducer producer = context.createProducer();
	    for (Message message : messages) {
		final UUID callId = UUID.randomUUID();
		final String corellationID = callId.toString();
		message.setJMSCorrelationID(corellationID);
		producer.send(destination, message);
		logger.DEBUG.log("%1$s JMS-Message was sent to %2$s", callId, MyJMSs.getNameOf(destination));
		logger.SUPER_TRACE.log("%1$s ... with JMSMessageID %2$s", callId, MyJMSs.getJMSMessageIDOf(message));
	    }
	} catch (JMSException e) {
	    throw MyJMSs.uchedked(e);
	}
    }

    @Override
    public Message receiveReplyOn(final UUID callId, final Message message, final long timeout)
	    throws ResponseNotReceivedException {

	final Destination replyToDestination;
	try {
	    replyToDestination = message.getJMSReplyTo();
	} catch (JMSException e) {
	    throw MyJMSs.uchedked(e);
	}
	if (replyToDestination == null)
	    throw MyExceptions.illegalArgumentFormat(
		    "%1$s JMS-Message %2$ is not configured as ReplyTo message. JMSReplyTo property is null",
		    callId, MyJMSs.getJMSMessageIDOf(message));
	logger.DEBUG.log("%1$s JMS-Reply started receiving from %2$s", callId, MyJMSs.getNameOf(replyToDestination));
	final Optional<String> corellationID = MyJMSs.optJMSCorellationIDOf(message);
	if (!corellationID.isPresent())
	    throw MyExceptions.illegalArgumentFormat(
		    "%1$s JMS-Message %2$ has no JMSMessageID property. This may be due to it's not sent.", callId,
		    message);
	final String messageSelector = String.format("JMSCorrelationID = '%1$s'", corellationID.get());
	try (final JMSConsumer consumer = context.createConsumer(replyToDestination, messageSelector)) {
	    logger.TRACE.log("%1$s ... with timeout of %2$s ms", callId, timeout);
	    logger.TRACE.log("%1$s ... with message selector \"%2$s\"", callId, messageSelector);
	    final Message reply = consumer.receive(timeout);
	    if (reply != null) {
		logger.DEBUG.log("%1$s JMS-Reply received from %2$s", callId, MyJMSs.getJMSDestination(reply));
		logger.SUPER_TRACE.log("%1$s ... with JMSMessageID %2$s", callId, MyJMSs.getJMSMessageIDOf(reply));
		logger.TRACE.log("%1$s ... with JMSCorrellationID %2$s", callId, MyJMSs.getJMSCorellationIDOf(reply));
		return reply;
	    } else
		logger.DEBUG.log("%1$s JMS-Reply NOT received in %2$s ms.", callId, timeout);

	    throw MyExceptions.format(ResponseNotReceivedException::new,
		    "%1$s Error receiving JMS-Reply from %2$s with %3$s",
		    callId, // 1
		    MyJMSs.getNameOf(replyToDestination), // 2
		    messageSelector // 3
	    );
	} finally {
	    if (replyToDestination instanceof TemporaryQueue) {
		try {
		    logger.DEBUG.log("%1$s Dropping temporary queue %2$s", callId,
			    MyJMSs.getNameOf(replyToDestination));
		    ((TemporaryQueue) replyToDestination).delete();
		    logger.TRACE.log("%1$s ... dropped", callId);
		} catch (JMSException ignored) {
		    logger.TRACE.log("%1$s ... not dropped with exception %2$s", callId, ignored.getMessage());
		}
	    }
	    if (replyToDestination instanceof TemporaryTopic) {
		try {
		    logger.DEBUG.log("%1$s Dropping temporary topic %2$s", callId,
			    MyJMSs.getNameOf(replyToDestination));
		    ((TemporaryTopic) replyToDestination).delete();
		    logger.TRACE.log("%1$s ... dropped.", callId);
		} catch (JMSException ignored) {
		    logger.TRACE.log("%1$s ... not dropped with exception %2$s", callId, ignored.getMessage());
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
	    Messages.propertiesToMessage(message, properties);
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
