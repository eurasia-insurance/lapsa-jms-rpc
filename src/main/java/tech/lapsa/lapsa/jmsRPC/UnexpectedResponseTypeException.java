package tech.lapsa.lapsa.jmsRPC;

import tech.lapsa.java.commons.exceptions.UnexpectedTypeException;

public final class UnexpectedResponseTypeException extends UnexpectedTypeException {
    private static final long serialVersionUID = 1L;

    public UnexpectedResponseTypeException(final Class<?> expectedClazz, final Class<?> actualClazz) {
	super(expectedClazz, actualClazz);
    }

    public UnexpectedResponseTypeException(final String message) {
	super(message);
    }
}