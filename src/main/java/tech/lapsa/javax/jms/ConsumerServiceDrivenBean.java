package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;

public abstract class ConsumerServiceDrivenBean<E extends Serializable>
	extends BaseDrivenBean<E, VoidResult> implements JmsReceiverService<E> {

    protected ConsumerServiceDrivenBean(final Class<E> entityClazz) {
	super(entityClazz);
    }

    @Override
    final VoidResult _apply(final E entity, final Properties properties) {
	receiving(entity, properties);
	return VoidResult.INSTANCE;
    }
}
