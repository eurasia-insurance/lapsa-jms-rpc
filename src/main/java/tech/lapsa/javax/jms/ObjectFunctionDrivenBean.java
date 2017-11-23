package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Optional;
import java.util.Properties;

import javax.jms.Connection;
import javax.validation.ValidationException;

public abstract class ObjectFunctionListener<T extends Serializable, R extends Serializable>
	extends BaseListener<T, R> {

    protected ObjectFunctionListener(final Class<T> objectClazz) {
	super(objectClazz);
    }

    protected abstract R apply(T t, Properties properties);

    protected abstract Connection getConnectionResource();

    @Override
    Optional<Connection> _optConnection() {
	return Optional.of(getConnectionResource());
    }

    @Override
    R _apply(T t, Properties p) throws RuntimeException {
	return apply(t, p);
    }

    @Override
    void _validationError(ValidationException e) {
    }
}
