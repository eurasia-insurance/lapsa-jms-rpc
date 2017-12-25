package tech.lapsa.lapsa.jmsRPC.client;

public final class ResponseNotReceivedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ResponseNotReceivedException() {
    }

    public ResponseNotReceivedException(final String message, final Throwable cause, final boolean enableSuppression,
	    final boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
    }

    public ResponseNotReceivedException(final String message, final Throwable cause) {
	super(message, cause);
    }

    public ResponseNotReceivedException(final String message) {
	super(message);
    }

    public ResponseNotReceivedException(final Throwable cause) {
	super(cause);
    }
}