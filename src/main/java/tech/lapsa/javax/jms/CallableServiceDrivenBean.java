package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;

import tech.lapsa.javax.jms.internal.JmsInternalServiceBaseDrivenBean;

public abstract class CallableServiceDrivenBean<E extends Serializable, R extends Serializable>
	extends JmsInternalServiceBaseDrivenBean<E, R> implements JmsCallableService<E, R> {

    protected CallableServiceDrivenBean(final Class<E> entityClazz) {
	super(entityClazz);
    }

    @Override
    protected final R _apply(final E entity, final Properties properties) {
	return calling(entity, properties);
    }
}
