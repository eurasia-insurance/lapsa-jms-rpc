package tech.lapsa.lapsa.jmsRPC.client.beans;

import java.io.Serializable;
import java.util.Properties;

import javax.jms.Destination;

import tech.lapsa.lapsa.jmsRPC.VoidResult;
import tech.lapsa.lapsa.jmsRPC.client.JmsConsumerClient;
import tech.lapsa.lapsa.jmsRPC.client.ejbBeans.JmsInternalClient;

class JmsConsumerImpl<E extends Serializable> extends GeneralClientImpl<E, VoidResult>
	implements JmsConsumerClient<E> {

    JmsConsumerImpl(final JmsInternalClient internalClient, final Destination destination) {
	super(VoidResult.class, internalClient, destination);
    }

    @Override
    public void accept(final E entity, final Properties properties) {
	final VoidResult outO = _send(entity, properties);
	if (outO == null)
	    throw new RuntimeException(VoidResult.class.getName() + " expected");
    }

    @Override
    public void accept(final E entity) {
	accept(entity, null);
    }
}
