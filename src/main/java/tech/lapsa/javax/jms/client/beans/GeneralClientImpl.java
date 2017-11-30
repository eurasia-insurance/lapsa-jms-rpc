package tech.lapsa.javax.jms.client.beans;

import java.io.Serializable;
import java.util.Properties;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyStreams;
import tech.lapsa.javax.jms.UnexpectedResponseTypeException;
import tech.lapsa.javax.jms.client.ResponseNotReceivedException;
import tech.lapsa.javax.jms.commons.MyJMSs;
import tech.lapsa.javax.jms.service.ejbBeans.JmsInternalClient;

class GeneralClientImpl<E extends Serializable, R extends Serializable> {

    private static final int DEFAULT_TIMEOUT = 20 * 1000; // 20 seconds

    final Destination destination;
    final Class<R> resultClazz;
    final JmsInternalClient internalClient;

    GeneralClientImpl(final Class<R> resultClazz, final JmsInternalClient internalClient, final Destination destination) {
	this.resultClazz = MyObjects.requireNonNull(resultClazz, "resultClazz");
	this.destination = MyObjects.requireNonNull(destination, "destination");
	this.internalClient = MyObjects.requireNonNull(internalClient, "internalClient");
    }

    @SafeVarargs
    final void _sendNoWait(final E... entities) {
	_sendNoWait(null, entities);
    }

    @SafeVarargs
    final void _sendNoWait(final Properties properties, final E... entities) {
	try {
	    Message[] messages = MyStreams.orEmptyOf(entities) //
		    .map(e -> internalClient.createMessage(e, properties)) //
		    .toArray(Message[]::new);
	    internalClient.send(destination, messages);
	} catch (JMSException e) {
	    throw MyJMSs.uchedked(e);
	}
    }

    final R _send(final E entity) {
	return _send(entity, null);
    }

    final R _send(final E entity, final Properties properties) {
	try {

	    final Message message = internalClient.createMessage(entity, properties);
	    internalClient.sendWithReplyTo(destination, message);
	    final Message reply = internalClient.receiveReplyOn(message, DEFAULT_TIMEOUT);

	    if (reply == null)
		throw new ResponseNotReceivedException();

	    if (reply.isBodyAssignableTo(resultClazz))
		return reply.getBody(resultClazz);

	    if (reply.isBodyAssignableTo(RuntimeException.class))
		throw reply.getBody(RuntimeException.class);

	    if (reply.isBodyAssignableTo(Serializable.class)) {
		final Object wrongTypedObject = reply.getBody(Object.class);
		if (wrongTypedObject != null)
		    throw new UnexpectedResponseTypeException(resultClazz, wrongTypedObject.getClass());
	    }

	    throw new UnexpectedResponseTypeException("Unknown response type");

	} catch (JMSException e) {
	    throw MyJMSs.uchedked(e);
	}
    }
}