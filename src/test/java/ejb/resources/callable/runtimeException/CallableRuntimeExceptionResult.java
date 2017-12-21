package ejb.resources.callable.runtimeException;

import java.io.Serializable;

public class CallableRuntimeExceptionResult implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PREFIX = "REPLY: ";

    private final String message;

    public CallableRuntimeExceptionResult(final CallableRuntimeExceptionEntity callableRuntimeExceptionEntity) {
	message = PREFIX + callableRuntimeExceptionEntity.getMessage();
    }

    @Override
    public String toString() {
	return message;
    }

    public String getMessage() {
	return message;
    }

}
