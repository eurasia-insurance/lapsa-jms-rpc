package tech.lapsa.javax.jms.client.beans;

import java.io.Serializable;
import java.util.Properties;

import javax.jms.Destination;

import tech.lapsa.javax.jms.VoidResult;
import tech.lapsa.javax.jms.client.JmsEventNotificatorClient;
import tech.lapsa.javax.jms.client.ejbBeans.JmsInternalClient;

class JmsEventNotificatorImpl<E extends Serializable> extends GeneralClientImpl<E, VoidResult>
	implements JmsEventNotificatorClient<E> {

    JmsEventNotificatorImpl(final JmsInternalClient client, final Destination destination) {
	super(VoidResult.class, client, destination);

    }

    @Override
    @SafeVarargs
    public final void eventNotify(final E... entities) {
	_sendNoWait(null, entities);
    }

    @Override
    public void eventNotify(final E entity, final Properties properties) {
	_sendNoWait(properties, entity);
    }
}