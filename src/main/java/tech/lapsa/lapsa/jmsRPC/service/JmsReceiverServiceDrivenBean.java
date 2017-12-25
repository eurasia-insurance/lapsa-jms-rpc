package tech.lapsa.lapsa.jmsRPC.service;

import java.io.Serializable;
import java.util.Properties;

import tech.lapsa.lapsa.jmsRPC.VoidResult;
import tech.lapsa.lapsa.jmsRPC.service.ejbBeans.JmsInternalServiceBaseDrivenBean;

public abstract class JmsReceiverServiceDrivenBean<E extends Serializable>
	extends JmsInternalServiceBaseDrivenBean<E, VoidResult> implements JmsReceiverService<E> {

    protected JmsReceiverServiceDrivenBean(final Class<E> entityClazz) {
	super(entityClazz);
    }

    @Override
    protected final VoidResult _apply(final E entity, final Properties properties) {
	receiving(entity, properties);
	return VoidResult.INSTANCE;
    }
}
