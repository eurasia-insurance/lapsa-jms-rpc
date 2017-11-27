package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;

public abstract class CallableServiceDrivenBean<E extends Serializable, R extends Serializable>
	extends BaseDrivenBean<E, R> implements JmsCallableService<E, R> {

    protected CallableServiceDrivenBean(final Class<E> entityClazz) {
	super(entityClazz);
    }

    @Override
    final R _apply(final E entity, final Properties properties) {
	return calling(entity, properties);
    }
}
