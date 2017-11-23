package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;

import javax.validation.ValidationException;

import tech.lapsa.javax.jms.MyJMSFunctions.VoidResult;

public abstract class ObjectConsumerDrivenBean<T extends Serializable>
	extends BaseDrivenBean<T, VoidResult> {

    protected ObjectConsumerDrivenBean(final Class<T> objectClazz) {
	super(objectClazz);
    }

    protected abstract void accept(T t, Properties properties);

    @Override
    VoidResult _apply(T t, Properties p) throws RuntimeException {
	accept(t, p);
	return new VoidResult();
    }

    @Override
    void _validationError(ValidationException e) {
    }
}
