package tech.lapsa.javax.jms.service;

import java.io.Serializable;
import java.util.Properties;

import tech.lapsa.javax.jms.VoidResult;
import tech.lapsa.javax.jms.service.ejbBeans.JmsInternalServiceBaseDrivenBean;

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
