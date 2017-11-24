package ejb.resources.function.runtimeException;

import java.io.Serializable;

public class FunctionRuntimeExceptionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;

    public FunctionRuntimeExceptionEntity(String message) {
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
