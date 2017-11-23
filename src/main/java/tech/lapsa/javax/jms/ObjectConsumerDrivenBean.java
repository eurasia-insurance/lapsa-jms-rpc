package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;

import javax.validation.ValidationException;

public abstract class ObjectConsumerDrivenBean<T extends Serializable>
	extends BaseDrivenBean<T, VoidResult> {

    protected ObjectConsumerDrivenBean(final Class<T> objectClazz) {
	super(objectClazz);
    }

    protected abstract void accept(T t, Properties properties);

    @Override
    final VoidResult _apply(final T t, final Properties p) throws RuntimeException {
	accept(t, p);
	return new VoidResult();
    }

    @Override
    final void _validationError(final ValidationException e) {
    }
}
