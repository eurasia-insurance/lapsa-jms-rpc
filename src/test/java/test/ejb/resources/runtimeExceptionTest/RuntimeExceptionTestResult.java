package test.ejb.resources.runtimeExceptionTest;

import java.io.Serializable;

public class RuntimeExceptionTestResult implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PREFIX = "REPLY: ";

    private final String message;

    public RuntimeExceptionTestResult(RuntimeExceptionTestEntity runtimeExceptionTestEntity) {
	this.message = PREFIX + runtimeExceptionTestEntity.getMessage();
    }

    @Override
    public String toString() {
	return message;
    }

    public String getMessage() {
	return message;
    }

}
