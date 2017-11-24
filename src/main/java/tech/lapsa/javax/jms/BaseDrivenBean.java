package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.MessageDrivenContext;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.logging.MyLogger;

abstract class BaseDrivenBean<E extends Serializable, R extends Serializable> implements MessageListener {

    private final MyLogger logger = MyLogger.newBuilder() //
	    .withNameOf(this.getClass()) //
	    .addLoggerNameAsPrefix() //
	    .build();

    private final Class<E> entityClazz;

    @Inject
    private ValidatorFactory validatorFactory;

    @Resource
    private MessageDrivenContext mdc;

    @Inject
    private JMSContext context;

    BaseDrivenBean(final Class<E> entityClazz) {
	this.entityClazz = entityClazz;
    }

    private E processedEntity(final Message entityM) throws JMSException {
	if (!entityM.isBodyAssignableTo(entityClazz)) {
	    final Object wrongTypedObject = entityM.getBody(Object.class);
	    if (wrongTypedObject != null)
		throw new UnexpectedTypeRequestedException(entityClazz, wrongTypedObject.getClass());
	}
	final E entity = entityM.getBody(entityClazz);
	if (entity != null) {
	    final Set<ConstraintViolation<Object>> violations = validatorFactory.getValidator().validate(entity);
	    if (violations != null && violations.size() > 0) {
		throw new MyConstraintViolationExcetpion(violations);
	    }
	}
	return entity;
    }

    @Override
    public void onMessage(final Message entityM) {
	try {
	    try {
		logger.FINE.log("JMS Message received '%1$s' from '%2$s'", entityM.getJMSMessageID(),
			destinationName(entityM.getJMSDestination()));
		final Properties properties = MyMessages.propertiesFromMessage(entityM);
		if (MyObjects.nonNull(properties))
		    logger.FINER.log("With properties '%1$s'", properties);
		final E entity = processedEntity(entityM);
		logger.FINER.log( //
			MyObjects.isNull(entity) //
				? "Entity '%1$s' is null" //
				: "Entity '%1$s' processed '%2$s'",
			entityClazz, entity);
		final R result = _apply(entity, properties);
		if (MyObjects.isNull(result))
		    logger.FINER.log("Result is null");
		else
		    logger.FINER.log("Result '%1$s' processed '%2$s'", result.getClass(), result);
		final Message resultM = reply(entityM, result);
		if (MyObjects.isNull(resultM))
		    logger.FINE.log("JMS Result was not sent due to it's null");
		else
		    logger.FINE.log("JMS Result was sent '%1$s' to '%2$s'", resultM.getJMSMessageID(),
			    destinationName(resultM.getJMSDestination()));
	    } catch (final RuntimeException e) {
		// also catches ValidationException types
		logger.WARN.log(e);
		final Message runtimeExceptionM = reply(entityM, e);
		if (MyObjects.isNull(runtimeExceptionM))
		    logger.FINE.log("JMS RuntimeException was not sent due to it's null");
		else
		    logger.FINE.log("JMS RuntimeException was sent '%1$s' to '%2$s'",
			    runtimeExceptionM.getJMSMessageID(),
			    destinationName(runtimeExceptionM.getJMSDestination()));
	    }
	} catch (final JMSException e) {
	    logger.SEVERE.log(e);
	    mdc.setRollbackOnly();
	}
    }

    public MyLogger logger() {
	return logger;
    }

    private static String destinationName(final Destination d) {
	return MyObjects.optionalA(d, Queue.class) //
		.map(x -> {
		    try {
			return x.getQueueName();
		    } catch (JMSException e) {
			return null;
		    }
		}) //
		.orElseGet(() -> MyObjects.optionalA(d, Topic.class) //
			.map(x -> {
			    try {
				return x.getTopicName();
			    } catch (JMSException e) {
				return null;
			    }
			}) //
			.orElse(null));
    }

    private Message reply(final Message entityM, final Serializable serializable) throws JMSException {
	final Destination replyToD = entityM.getJMSReplyTo();
	if (replyToD == null) // for noWait senders support
	    return null;
	final Message resultM = context.createObjectMessage(serializable);
	resultM.setJMSCorrelationID(entityM.getJMSMessageID());
	context.createProducer()
		.send(replyToD, resultM);
	return resultM;
    }

    abstract R _apply(E entity, Properties properties);

}