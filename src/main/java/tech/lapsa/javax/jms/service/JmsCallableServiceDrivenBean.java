package tech.lapsa.javax.jms.service;

import java.io.Serializable;
import java.util.Properties;

import tech.lapsa.javax.jms.service.ejbBeans.JmsInternalServiceBaseDrivenBean;

public abstract class JmsCallableServiceDrivenBean<E extends Serializable, R extends Serializable>
	extends JmsInternalServiceBaseDrivenBean<E, R> implements JmsCallableService<E, R> {

    protected JmsCallableServiceDrivenBean(final Class<E> entityClazz) {
	super(entityClazz);
    }

    @Override
    protected final R _apply(final E entity, final Properties properties) {
	return calling(entity, properties);
    }
}
