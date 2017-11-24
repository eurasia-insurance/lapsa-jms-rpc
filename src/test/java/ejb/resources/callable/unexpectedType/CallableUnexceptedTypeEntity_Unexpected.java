package ejb.resources.callable.unexpectedType;

import java.io.Serializable;

public class CallableUnexceptedTypeEntity_Unexpected implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;

    public CallableUnexceptedTypeEntity_Unexpected(String message) {
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
