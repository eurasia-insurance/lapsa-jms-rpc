package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;
import java.util.Set;
import java.util.StringJoiner;

import javax.annotation.Resource;
import javax.ejb.MessageDrivenContext;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageFormatException;
import javax.jms.MessageListener;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
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

    private E validatedObject(final Message entityM) throws JMSException, ValidationException {
	try {
	    final E entity = entityM.getBody(entityClazz);
	    final Set<ConstraintViolation<Object>> violations = validatorFactory.getValidator().validate(entity);
	    if (violations != null && violations.size() > 0)
		throw new ValidationException(violationsString(violations));
	    return entity;
	} catch (final MessageFormatException e) {
	    throw new ValidationException(String.format("Message is not a %1$s type", entityClazz.getName()));
	}
    }

    private String violationsString(final Set<ConstraintViolation<Object>> violations) {
	final StringJoiner sj = new StringJoiner(System.lineSeparator());
	sj.setEmptyValue("With no violations");
	violations.stream() //
		.map(cb -> String.format("Property: %1$s, value: %2$s, message: %3$s " + System.lineSeparator(), //
			cb.getPropertyPath(), //
			cb.getInvalidValue(), //
			cb.getMessage())) //
		.forEach(sj::add);
	return sj.toString();
    }

    @Override
    public final void onMessage(final Message entityM) {
	try {
	    try {
		final Properties properties = MyMessages.propertiesFromMessage(entityM);
		final E entity = validatedObject(entityM);
		final R result = _apply(entity, properties);
		reply(entityM, result);
	    } catch (final ValidationException e) {
		logger.FINE.log(e);
		reply(entityM, e);
		mdc.setRollbackOnly();
	    } catch (final RuntimeException e) {
		logger.WARN.log(e);
		reply(entityM, e);
		mdc.setRollbackOnly();
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
		.setJMSCorrelationID(entityM.getJMSCorrelationID()) // TODO DEBUG : MessageID colud be used
		.send(replyToD, serializable);
    }

    abstract R _apply(E entity, Properties properties);
}