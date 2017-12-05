package tech.lapsa.javax.jms.service.ejbBeans;

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
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.ValidatorFactory;

import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.logging.MyLogger;
import tech.lapsa.java.commons.logging.MyLogger.MyLevel;
import tech.lapsa.java.commons.reflect.MyAnnotations;
import tech.lapsa.javax.jms.Messages;
import tech.lapsa.javax.jms.UnexpectedTypeRequestedException;
import tech.lapsa.javax.jms.commons.MyJMSs;
import tech.lapsa.javax.jms.service.JmsSkipValidation;

public abstract class JmsInternalServiceBaseDrivenBean<E extends Serializable, R extends Serializable>
	implements MessageListener {

    private final MyLogger logger = MyLogger.newBuilder() //
	    .withNameOf(this.getClass()) //
	    .addInstantPrefix() //
	    .addPrefix("JMS-Service") //
	    .build();

    private final MyLevel debugLevel = logger.FINE;
    private final MyLevel traceLevel = logger.FINER;
    private final MyLevel superTraceLevel = logger.FINEST;

    private final Class<E> entityClazz;

    @Inject
    private ValidatorFactory validatorFactory;

    @Resource
    private MessageDrivenContext mdc;

    @Inject
    private JMSContext context;

    private final boolean validationRequired;

    protected JmsInternalServiceBaseDrivenBean(final Class<E> entityClazz) {
	this.entityClazz = entityClazz;
	this.validationRequired = MyAnnotations.notAnnotatedSupers(this.getClass(), JmsSkipValidation.class);
    }

    private E processedEntity(final Message entityM) throws JMSException {
	if (!entityM.isBodyAssignableTo(entityClazz)) {
	    final Object wrongTypedObject = entityM.getBody(Object.class);
	    if (wrongTypedObject != null)
		throw new UnexpectedTypeRequestedException(entityClazz, wrongTypedObject.getClass());
	}
	final E entity = entityM.getBody(entityClazz);
	if (entity != null && validationRequired) {
	    final Set<ConstraintViolation<Object>> violations = validatorFactory.getValidator().validate(entity);
	    if (violations != null && violations.size() > 0) {
		final MyConstraintViolationException viol = new MyConstraintViolationException(violations);
		logger.WARN.log("Entity validation FAILED with message '%1$s'", viol.getMessage());
		throw viol;
	    }
	}
	return entity;
    }

    @Override
    public void onMessage(final Message entityM) {
	try {
	    debugLevel.log("JMS Message received from %1$s", MyJMSs.getNameOf(entityM.getJMSDestination()));
	    superTraceLevel.log("... with JMSMessageID %1$s", MyJMSs.getJMSMessageIDOf(entityM));
	    traceLevel.log("... with JMSCorrellationID %1$s", MyJMSs.getJMSCorellationIDOf(entityM));

	    final Properties properties = Messages.propertiesFromMessage(entityM);
	    if (MyObjects.nonNull(properties))
		traceLevel.log("... with properties '%1$s'", properties);

	    debugLevel.log("Processing entity...");
	    final E entity;
	    try {
		entity = processedEntity(entityM);
		debugLevel.log("Entity processed sucessfuly");
		if (MyObjects.isNull(entity))
		    traceLevel.log("... with null entity");
		else {
		    traceLevel.log("... with entity of type '%1$s'", entityClazz);
		    superTraceLevel.log("... with entity's toString() '%1$s'", entity);
		}
	    } catch (final ValidationException | UnexpectedTypeRequestedException e) {
		debugLevel.log("Processing entity failed with %$1$s occured", e);
		logger.WARN.log(e);
		replyException(entityM, e);
		return;
	    }

	    debugLevel.log("Calling delegate...");
	    final R result;
	    try {
		result = _apply(entity, properties);
		debugLevel.log("Delegate called sucessfuly");
		if (MyObjects.isNull(result))
		    traceLevel.log("... with null result");
		else {
		    traceLevel.log("... with result of type '%1$s'", result.getClass());
		    superTraceLevel.log("... with result's toString() '%1$s'", result);
		}
		replyResult(entityM, result);
	    } catch (final RuntimeException e) {
		debugLevel.log("Delegate call failed with %1$s occured", e);
		logger.SEVERE.log(e);
		replyException(entityM, e);
		return;
	    }
	} catch (final JMSException | RuntimeException e) {
	    logger.SEVERE.log(e);
	    mdc.setRollbackOnly();
	}
    }

    private void replyResult(final Message entityM, final R result) throws JMSException {
	if (isReplyRequired(entityM)) {
	    final Message replyM = reply(entityM, result);
	    if (MyObjects.isNull(replyM))
		debugLevel.log("JMS-Reply Message was not sent due to it's null");
	    else {
		debugLevel.log("JMS-Reply Message was sent '%1$s'", MyJMSs.getJMSDestination(replyM));
		superTraceLevel.log("... with JMSMessageID %1$s", MyJMSs.getJMSMessageIDOf(replyM));
		traceLevel.log("... with JMSCorrellationID %1$s", MyJMSs.getJMSCorellationIDOf(replyM));
	    }
	}
    }

    private void replyException(final Message entityM, final RuntimeException e) throws JMSException {
	if (isReplyRequired(entityM)) {
	    final Message replyM = reply(entityM, e);
	    if (MyObjects.isNull(replyM))
		debugLevel.log("JMS-Reply Exception was not sent due to it's null");
	    else {
		debugLevel.log("JMS-Reply Exception was sent %1$s", MyJMSs.getJMSDestination(replyM));
		superTraceLevel.log("... with JMSMessageID %1$s", MyJMSs.getJMSMessageIDOf(replyM));
		traceLevel.log("... with JMSCorrellationID %1$s", MyJMSs.getJMSCorellationIDOf(replyM));
	    }
	}
    }

    public MyLogger logger() {
	return logger;
    }

    private boolean isReplyRequired(final Message entityM) throws JMSException {
	final boolean required = entityM.getJMSReplyTo() != null;
	if (!required)
	    debugLevel.log("JMS-Reply is not required");
	return required;
    }

    private Message reply(final Message entityM, final Serializable serializable) throws JMSException {
	final Destination replyToD = entityM.getJMSReplyTo();
	final Message resultM = context.createObjectMessage(serializable);
	resultM.setJMSCorrelationID(entityM.getJMSCorrelationID());
	context.createProducer()
		.send(replyToD, resultM);
	return resultM;
    }

    protected abstract R _apply(E entity, Properties properties);

}