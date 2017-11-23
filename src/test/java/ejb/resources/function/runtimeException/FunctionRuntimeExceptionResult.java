package test.ejb.resources.function.runtimeException;

import java.io.Serializable;

public class FunctionRuntimeExceptionResult implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PREFIX = "REPLY: ";

    private final String message;

    public FunctionRuntimeExceptionResult(FunctionRuntimeExceptionEntity functionRuntimeExceptionEntity) {
	this.message = PREFIX + functionRuntimeExceptionEntity.getMessage();
    }

    @Override
    public String toString() {
	return message;
    }

    public String getMessage() {
	return message;
    }

}
