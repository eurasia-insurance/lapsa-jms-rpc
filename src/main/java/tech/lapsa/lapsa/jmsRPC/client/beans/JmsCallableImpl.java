package tech.lapsa.lapsa.jmsRPC.client.beans;

import java.io.Serializable;
import java.util.Properties;

import javax.jms.Destination;

import tech.lapsa.lapsa.jmsRPC.client.JmsCallableClient;
import tech.lapsa.lapsa.jmsRPC.client.ejbBeans.JmsInternalClient;

class JmsCallableImpl<E extends Serializable, R extends Serializable> extends GeneralClientImpl<E, R>
	implements JmsCallableClient<E, R> {

    JmsCallableImpl(final Class<R> resultClazz, final JmsInternalClient client,
	    final Destination destination) {
	super(resultClazz, client, destination);
    }

    @Override
    public R call(final E entity, final Properties properties) {
	return _send(entity, properties);
    }

    @Override
    public R call(final E entity) {
	return call(entity, null);
    }
}