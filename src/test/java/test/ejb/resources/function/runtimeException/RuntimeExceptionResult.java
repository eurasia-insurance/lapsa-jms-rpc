package test.ejb.resources.function.runtimeException;

import java.io.Serializable;

public class RuntimeExceptionResult implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PREFIX = "REPLY: ";

    private final String message;

    public RuntimeExceptionResult(RuntimeExceptionEntity runtimeExceptionEntity) {
	this.message = PREFIX + runtimeExceptionEntity.getMessage();
    }

    @Override
    public String toString() {
	return message;
    }

    public String getMessage() {
	return message;
    }

}
