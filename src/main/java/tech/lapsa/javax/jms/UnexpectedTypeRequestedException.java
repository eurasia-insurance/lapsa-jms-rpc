package tech.lapsa.javax.jms;

import tech.lapsa.java.commons.exceptions.UnexpectedTypeException;

public final class UnexpectedTypeRequestedException extends UnexpectedTypeException {
    private static final long serialVersionUID = 1L;

    public UnexpectedTypeRequestedException(Class<?> expectedClazz, Class<?> actualClazz) {
	super(expectedClazz, actualClazz);
    }

    public UnexpectedTypeRequestedException(String message) {
	super(message);
    }
}