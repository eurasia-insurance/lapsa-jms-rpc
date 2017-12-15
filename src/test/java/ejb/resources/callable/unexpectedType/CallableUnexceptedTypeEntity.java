package ejb.resources.callable.unexpectedType;

import java.io.Serializable;

public class CallableUnexceptedTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;

    public CallableUnexceptedTypeEntity(final String message) {
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
