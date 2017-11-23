package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;

public abstract class ObjectFunctionDrivenBean<T extends Serializable, R extends Serializable>
	extends BaseDrivenBean<T, R> {

    protected ObjectFunctionDrivenBean(final Class<T> objectClazz) {
	super(objectClazz);
    }

    protected abstract R apply(T t, Properties properties);

    @Override
    final R _apply(final T t, final Properties p) {
	return apply(t, p);
    }
}
