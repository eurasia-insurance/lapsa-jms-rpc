package ejb.resources.callable.runtimeException;

import java.io.Serializable;

public class CallableRuntimeExceptionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;

    public CallableRuntimeExceptionEntity(String message) {
	this.message = message;
    }

    @Override
    public String toString() {
	return message;
    }

    public String getMessage() {
	return message;
    }
}
