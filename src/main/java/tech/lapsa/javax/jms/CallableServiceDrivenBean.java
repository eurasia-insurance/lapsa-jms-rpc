package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;

public abstract class ObjectFunctionDrivenBean<E extends Serializable, R extends Serializable>
	extends BaseDrivenBean<E, R> {

    protected ObjectFunctionDrivenBean(final Class<E> entityClazz) {
	super(entityClazz);
    }

    protected abstract R apply(E entity, Properties properties);

    @Override
    final R _apply(final E entity, final Properties properties) {
	return apply(entity, properties);
    }
}
