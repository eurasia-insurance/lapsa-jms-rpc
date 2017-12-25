package tech.lapsa.lapsa.jmsRPC.service.ejbBeans;

import java.io.Serializable;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

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
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.logging.MyLogger;
import tech.lapsa.java.commons.logging.MyLogger.MyLevel;
import tech.lapsa.java.commons.reflect.MyAnnotations;
import tech.lapsa.java.commons.util.MyUUIDs;
import tech.lapsa.javax.jms.commons.MyJMSs;
import tech.lapsa.lapsa.jmsRPC.Messages;
import tech.lapsa.lapsa.jmsRPC.UnexpectedTypeRequestedException;
import tech.lapsa.lapsa.jmsRPC.service.JmsSkipValidation;

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

    private E processedEntity(final UUID callId, final Message entityM) throws JMSException {
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
		logger.WARN.log("%1$s Entity validation FAILED with message '%2$s'", callId, viol.getMessage());
		throw viol;
	    }
	}
	return entity;
    }

    @Override
    public void onMessage(final Message entityM) {

	final UUID callId = MyOptionals.of(entityM) //
		.flatMap(MyJMSs::optJMSCorellationIDOf) //
		.flatMap(MyUUIDs::optOf) //
		.orElse(UUID.randomUUID());

	try {
	    debugLevel.log("%1$s JMS Message received from %2$s", callId,
		    MyJMSs.getNameOf(entityM.getJMSDestination()));
	    superTraceLevel.log("%1$s ... with JMSMessageID %2$s", callId, MyJMSs.getJMSMessageIDOf(entityM));
	    traceLevel.log("%1$s ... with JMSCorrellationID %2$s", callId, MyJMSs.getJMSCorellationIDOf(entityM));

	    final Properties properties = Messages.propertiesFromMessage(entityM);
	    if (MyObjects.nonNull(properties))
		traceLevel.log("%1$s ... with properties '%2$s'", callId, properties);

	    debugLevel.log("%1$s Processing entity...", callId);
	    final E entity;
	    try {
		entity = processedEntity(callId, entityM);
		debugLevel.log("%1$s Entity processed sucessfuly", callId);
		if (MyObjects.isNull(entity))
		    traceLevel.log("%1$s ... with null entity", callId);
		else {
		    traceLevel.log("%1$s ... with entity of type '%2$s'", callId, entityClazz);
		    superTraceLevel.log("%1$s ... with entity's toString() '%2$s'", callId, entity);
		}
	    } catch (final ValidationException | UnexpectedTypeRequestedException e) {
		debugLevel.log("%1$s Processing entity failed with %2$s occured", callId, e);
		logger.WARN.log(e);
		replyException(callId, entityM, e);
		return;
	    }

	    debugLevel.log("%1$s Calling delegate...", callId);
	    final R result;
	    try {
		result = _apply(entity, properties);
		debugLevel.log("%1$s Delegate called sucessfuly", callId);
		if (MyObjects.isNull(result))
		    traceLevel.log("%1$s ... with null result", callId);
		else {
		    traceLevel.log("%1$s ... with result of type '%2$s'", callId, result.getClass());
		    superTraceLevel.log("%1$s ... with result's toString() '%2$s'", callId, result);
		}
		replyResult(callId, entityM, result);
	    } catch (final RuntimeException e) {
		debugLevel.log("%1$s Delegate call failed with %2$s occured", callId, e);
		logger.SEVERE.log(e);
		replyException(callId, entityM, e);
		return;
	    }
	} catch (final JMSException | RuntimeException e) {
	    logger.SEVERE.log(e);
	    mdc.setRollbackOnly();
	}
    }

    private void replyResult(final UUID callId, final Message entityM, final R result) throws JMSException {
	if (isReplyRequired(callId, entityM)) {
	    final Message replyM = reply(entityM, result);
	    if (MyObjects.isNull(replyM))
		debugLevel.log("%1$s JMS-Reply Message was not sent due to it's null", callId);
	    else {
		debugLevel.log("%1$s JMS-Reply Message was sent '%2$s'", callId, MyJMSs.getJMSDestination(replyM));
		superTraceLevel.log("%1$s ... with JMSMessageID %2$s", callId, MyJMSs.getJMSMessageIDOf(replyM));
		traceLevel.log("%1$s ... with JMSCorrellationID %2$s", callId, MyJMSs.getJMSCorellationIDOf(replyM));
	    }
	}
    }

    private void replyException(final UUID callId, final Message entityM, final RuntimeException e)
	    throws JMSException {
	if (isReplyRequired(callId, entityM)) {
	    final Message replyM = reply(entityM, e);
	    if (MyObjects.isNull(replyM))
		debugLevel.log("%1$s JMS-Reply Exception was not sent due to it's null", callId);
	    else {
		debugLevel.log("%1$s JMS-Reply Exception was sent %2$s", callId, MyJMSs.getJMSDestination(replyM));
		superTraceLevel.log("%1$s ... with JMSMessageID %2$s", callId, MyJMSs.getJMSMessageIDOf(replyM));
		traceLevel.log("%1$s ... with JMSCorrellationID %2$s", callId, MyJMSs.getJMSCorellationIDOf(replyM));
	    }
	}
    }

    public MyLogger logger() {
	return logger;
    }

    private boolean isReplyRequired(final UUID callId, final Message entityM) throws JMSException {
	final boolean required = entityM.getJMSReplyTo() != null;
	if (!required)
	    debugLevel.log("%1$s JMS-Reply is not required", callId);
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