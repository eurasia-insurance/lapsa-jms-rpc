package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Optional;
import java.util.Properties;

import javax.jms.Connection;
import javax.validation.ValidationException;

public abstract class ObjectConsumerListener<T extends Serializable> extends BaseListener<T, Serializable> {

    protected ObjectConsumerListener(final Class<T> objectClazz) {
	super(objectClazz);
    }

    protected abstract void accept(T t, Properties properties);

    @Override
    Optional<Connection> _optConnection() {
	return Optional.empty();
    }

    @Override
    Serializable _apply(T t, Properties p) throws RuntimeException {
	accept(t, p);
	return null;
    }

    @Override
    void _validationError(ValidationException e) {
    }
}
