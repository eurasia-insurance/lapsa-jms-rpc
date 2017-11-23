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

abstract class BaseDrivenBean<I extends Serializable, O extends Serializable> implements MessageListener {

    private final MyLogger logger = MyLogger.newBuilder() //
	    .withNameOf(this.getClass()) //
	    .build();

    private final Class<I> inC;

    @Inject
    private ValidatorFactory validatorFactory;

    @Resource
    private MessageDrivenContext mdc;

    @Inject
    @JMSConnectionFactory(Constants.JNDI_DEFAULT_JMS_CONNECTION_FACTORY)
    private JMSContext context;

    BaseDrivenBean(final Class<I> inC) {
	this.inC = inC;
    }

    private I validatedObject(final Message inM) throws JMSException, ValidationException {
	try {
	    final I in = inM.getBody(inC);
	    final Set<ConstraintViolation<Object>> violations = validatorFactory.getValidator().validate(in);
	    if (violations != null && violations.size() > 0)
		throw new ValidationException(violationsString(violations));
	    return in;
	} catch (final MessageFormatException e) {
	    throw new ValidationException(String.format("Message is not a %1$s type", inC.getName()));
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
    public final void onMessage(final Message inM) {
	try {
	    try {
		final Properties p = MyMessages.propertiesFromMessage(inM);
		final I in = validatedObject(inM);
		final O out = _apply(in, p);
		reply(inM, out);
	    } catch (final ValidationException e) {
		logger.FINE.log(e);
		reply(inM, e);
		mdc.setRollbackOnly();
	    } catch (final RuntimeException e) {
		logger.WARN.log(e);
		reply(inM, e);
		mdc.setRollbackOnly();
	    }
	} catch (final JMSException e) {
	    logger.SEVERE.log(e);
	    mdc.setRollbackOnly();
	}
    }

    private void reply(final Message inM, final Serializable serializable) throws JMSException {
	final Destination outD = inM.getJMSReplyTo();
	if (outD == null) // for noWait senders support
	    return;
	context.createProducer()
		.setJMSCorrelationID(inM.getJMSCorrelationID()) // TODO DEBUG : MessageID colud be used
		.send(outD, serializable);
    }

    abstract O _apply(I in, Properties p);
}