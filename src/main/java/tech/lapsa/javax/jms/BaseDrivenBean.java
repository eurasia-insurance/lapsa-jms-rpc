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
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageFormatException;
import javax.jms.MessageListener;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.ValidatorFactory;

import tech.lapsa.java.commons.logging.MyLogger;

public abstract class BaseDrivenBean<IN extends Serializable, OUT extends Serializable> implements MessageListener {

    private final MyLogger logger = MyLogger.newBuilder() //
	    .withNameOf(this.getClass()) //
	    .build();

    private final Class<IN> inC;

    @Inject
    private ValidatorFactory validatorFactory;

    @Resource
    private MessageDrivenContext mdc;

    @Inject
    @JMSConnectionFactory(Constants.JNDI_DEFAULT_JMS_CONNECTION_FACTORY)
    private JMSContext context;

    BaseDrivenBean(Class<IN> inC) {
	this.inC = inC;
    }

    private IN validatedObject(final Message inM) throws JMSException, ValidationException {
	try {
	    IN inO = inM.getBody(inC);
	    Set<ConstraintViolation<Object>> violations = validatorFactory.getValidator().validate(inO);
	    if (violations != null && violations.size() > 0)
		throw new ValidationException(violationsString(violations));
	    return inO;
	} catch (MessageFormatException e) {
	    throw new ValidationException(String.format("Message is not a %1$s type", inC.getName()));
	}
    }

    private String violationsString(Set<ConstraintViolation<Object>> violations) {
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
    public void onMessage(Message originM) {
	try {
	    try {
		final Properties p = MyMessages.propertiesFromMessage(originM);
		final IN t = validatedObject(originM);
		final OUT r = _apply(t, p);
		reply(originM, r);
	    } catch (final ValidationException e) {
		logger.FINE.log(e);
		reply(originM, e);
		mdc.setRollbackOnly();
	    } catch (final RuntimeException e) {
		logger.WARN.log(e);
		reply(originM, e);
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
	final JMSProducer producer = context.createProducer();
	final Message outM = context.createObjectMessage(serializable);
	outM.setJMSCorrelationID(inM.getJMSMessageID());
	producer.send(outD, outM);
	logger.FINE.log("Rplying %1$s", outM);
    }

    abstract OUT _apply(IN inO, Properties p) throws RuntimeException;

    abstract void _validationError(ValidationException e);
}