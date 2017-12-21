package tech.lapsa.javax.jms;

import tech.lapsa.java.commons.exceptions.UnexpectedTypeException;

public final class UnexpectedTypeRequestedException extends UnexpectedTypeException {
    private static final long serialVersionUID = 1L;

    public UnexpectedTypeRequestedException(final Class<?> expectedClazz, final Class<?> actualClazz) {
	super(expectedClazz, actualClazz);
    }

    public UnexpectedTypeRequestedException(final String message) {
	super(message);
    }
}