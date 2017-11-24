package ejb.resources.callable.simple;

import java.io.Serializable;

public class CallableSimpleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;

    public CallableSimpleEntity(String message) {
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
