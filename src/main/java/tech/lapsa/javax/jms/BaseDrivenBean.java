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

import tech.lapsa.java.commons.logging.MyLogger;

abstract class BaseDrivenBean<E extends Serializable, R extends Serializable> implements MessageListener {

    private final MyLogger logger = MyLogger.newBuilder() //
	    .withNameOf(this.getClass()) //
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
		final Properties properties = MyMessages.propertiesFromMessage(entityM);
		final E entity = processedEntity(entityM);
		final R result = _apply(entity, properties);
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