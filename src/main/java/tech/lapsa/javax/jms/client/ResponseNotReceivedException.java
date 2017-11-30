package tech.lapsa.javax.jms.client;

public final class ResponseNotReceivedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ResponseNotReceivedException() {
    }

    public ResponseNotReceivedException(String message, Throwable cause, boolean enableSuppression,
	    boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
    }

    public ResponseNotReceivedException(String message, Throwable cause) {
	super(message, cause);
    }

    public ResponseNotReceivedException(String message) {
	super(message);
    }

    public ResponseNotReceivedException(Throwable cause) {
	super(cause);
    }
}