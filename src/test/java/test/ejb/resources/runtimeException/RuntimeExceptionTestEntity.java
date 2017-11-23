package test.ejb.resources.runtimeExceptionTest;

import java.io.Serializable;

public class RuntimeExceptionTestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;

    public RuntimeExceptionTestEntity(String message) {
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
