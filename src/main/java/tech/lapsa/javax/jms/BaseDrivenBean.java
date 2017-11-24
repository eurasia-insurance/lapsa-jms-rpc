package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.MessageDrivenContext;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidatorFactory;

import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.logging.MyLogger;

abstract class BaseDrivenBean<E extends Serializable, R extends Serializable> implements MessageListener {

    private final MyLogger logger = MyLogger.newBuilder() //
	    .withNameOf(this.getClass()) //
	    .addWithPrefix("JMS_SERVICE") //
	    .build();

    private final Class<E> entityClazz;

    @Inject
    private ValidatorFactory validatorFactory;

    @Resource
    private MessageDrivenContext mdc;

    @Inject
    @JMSConnectionFactory(Constants.JNDI_DEFAULT_JMS_CONNECTION_FACTORY)
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
	    if (violations != null && violations.size() > 0)
		throw new ConstraintViolationException(violations);
	}
	return entity;
    }

    @Override
    public void onMessage(final Message entityM) {
	try {
	    try {
		logger.FINER.log("Message received %1$s from %2$s", entityM.getJMSMessageID(),
			entityM.getJMSDestination());
		final Properties properties = MyMessages.propertiesFromMessage(entityM);
		if (MyObjects.nonNull(properties))
		    logger.FINER.log("With properties %1$s", properties);
		final E entity = processedEntity(entityM);
		logger.FINER.log( //
			MyObjects.isNull(entity) //
				? "Entity %1$s is null" //
				: "Entity %1$s processed %2$s",
			entityClazz, entity);
		final R result = _apply(entity, properties);
		logger.FINER.log( //
			MyObjects.isNull(entity) //
				? "Result is null" //
				: "Result %1$s processed %2$s",
			result.getClass(), result);
		reply(entityM, result);
	    } catch (final RuntimeException e) {
		// also catches ValidationException types
		logger.WARN.log(e);
		reply(entityM, e);
	    }
	} catch (final JMSException e) {
	    logger.SEVERE.log(e);
	    mdc.setRollbackOnly();
	}
    }

    private void reply(final Message entityM, final Serializable serializable) throws JMSException {
	final Destination replyToD = entityM.getJMSReplyTo();
	if (replyToD == null) // for noWait senders support
	    return;
	context.createProducer()
		.setJMSCorrelationID(entityM.getJMSMessageID())
		.send(replyToD, serializable);
    }

    abstract R _apply(E entity, Properties properties);
}