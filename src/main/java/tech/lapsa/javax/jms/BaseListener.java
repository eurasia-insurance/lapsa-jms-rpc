package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.StringJoiner;

import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageFormatException;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.ValidatorFactory;

import tech.lapsa.java.commons.logging.MyLogger;

public abstract class BaseListener<T extends Serializable, R extends Serializable> implements MessageListener {

    private final MyLogger logger = MyLogger.newBuilder() //
	    .withNameOf(this.getClass()) //
	    .build();

    private final Class<T> objectClazz;

    @Inject
    private ValidatorFactory validatorFactory;

    BaseListener(Class<T> objectClazz) {
	this.objectClazz = objectClazz;
    }

    private Properties processProperties(final Message message) throws JMSException {
	final Properties p = new Properties();
	final Enumeration<?> en = message.getPropertyNames();
	while (en.hasMoreElements()) {
	    String k = en.nextElement().toString();
	    if (k.startsWith("JMS"))
		continue;
	    String v;
	    try {
		v = message.getStringProperty(k);
		p.setProperty(k, v);
	    } catch (MessageFormatException ignored) {
	    }
	}
	return p;
    }

    private T validatedObject(final Message message) throws JMSException, ValidationException {
	try {
	    T obj = message.getBody(objectClazz);
	    Set<ConstraintViolation<Object>> violations = validatorFactory.getValidator().validate(obj);
	    if (violations != null && violations.size() > 0)
		throw new ValidationException(violationsString(violations));
	    return obj;
	} catch (MessageFormatException e) {
	    throw new ValidationException(String.format("Message is not a %1$s type", objectClazz.getName()));
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
    public void onMessage(Message message) {
	try {
	    try {
		Properties p = processProperties(message);
		T t = validatedObject(message);
		R r = _apply(t, p);
		reply(message, r);
	    } catch (ValidationException e) {
		logger.FINE.log(e);
		reply(message, e);
	    } catch (RuntimeException e) {
		logger.WARN.log(e);
		reply(message, e);
	    }

	} catch (JMSException e) {
	    logger.SEVERE.log(e);
	}
    }

    private void reply(final Message originMessage, final Serializable replyObj) throws JMSException {
	final Optional<Connection> oc = _optConnection();
	if (oc == null || !oc.isPresent())
	    return;
	final Destination replyDest = originMessage.getJMSReplyTo();
	if (replyDest == null)
	    return;
	try (Connection connection = oc.get();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageProducer producer = session.createProducer(replyDest)) {
	    Message reply = session.createObjectMessage(replyObj);
	    reply.setJMSCorrelationID(originMessage.getJMSMessageID());
	    producer.send(reply);
	    logger.FINE.log("Rplying %1$s", reply);
	}
    }

    abstract Optional<Connection> _optConnection();

    abstract R _apply(T t, Properties p) throws RuntimeException;
    
    abstract void _validationError(ValidationException e);
}