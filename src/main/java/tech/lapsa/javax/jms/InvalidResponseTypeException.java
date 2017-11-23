package tech.lapsa.javax.jms;

public final class InvalidResponseTypeException extends Exception {
    private static final long serialVersionUID = 1L;

    InvalidResponseTypeException(final Class<?> expected, final Class<?> actual) {
	super(String.format("Expected type is %1$s but the actual type was %2$s", expected, actual));
    }

    InvalidResponseTypeException(final String message) {
	super(message);
    }
}