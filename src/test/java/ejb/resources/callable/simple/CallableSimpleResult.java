package ejb.resources.callable.simple;

import java.io.Serializable;

public class CallableSimpleResult implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PREFIX = "REPLY: ";

    private final String message;
    private String name;

    public CallableSimpleResult(final CallableSimpleEntity callableSimpleEntity) {
	message = PREFIX + callableSimpleEntity.getMessage();
    }

    @Override
    public String toString() {
	return message;
    }

    public String getMessage() {
	return String.format(message, name);
    }

    public String getName() {
	return name;
    }

    public void setName(final String name) {
	this.name = name;
    }
}
