package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;

public abstract class ConsumerServiceDrivenBean<E extends Serializable>
	extends BaseDrivenBean<E, VoidResult> {

    protected ConsumerServiceDrivenBean(final Class<E> entityClazz) {
	super(entityClazz);
    }

    protected abstract void accept(E entity, Properties properties);

    @Override
    final VoidResult _apply(final E entity, final Properties properties) {
	accept(entity, properties);
	return VoidResult.INSTANCE;
    }
}
