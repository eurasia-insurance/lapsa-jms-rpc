package tech.lapsa.javax.jms;

public class InvalidResponseTypeException extends Exception {
    private static final long serialVersionUID = 1L;

    InvalidResponseTypeException(Class<?> expected, Class<?> actual) {
	super(String.format("Expected type is %1$s but the actual type was %2$s", expected, actual));
    }

    InvalidResponseTypeException(String message) {
	super(message);
    }
}