package test.ejb.resources.runtimeException;

import java.io.Serializable;

public class RuntimeExceptionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;

    public RuntimeExceptionEntity(String message) {
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
