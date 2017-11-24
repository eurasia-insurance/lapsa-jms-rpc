package tech.lapsa.javax.jms;

import tech.lapsa.java.commons.exceptions.UnexpectedTypeException;

public final class UnexpectedResponseTypeException extends UnexpectedTypeException {
    private static final long serialVersionUID = 1L;

    public UnexpectedResponseTypeException(Class<?> expectedClazz, Class<?> actualClazz) {
	super(expectedClazz, actualClazz);
    }

    public UnexpectedResponseTypeException(String message) {
	super(message);
    }
}