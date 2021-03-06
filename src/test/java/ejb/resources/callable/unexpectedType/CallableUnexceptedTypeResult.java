package ejb.resources.callable.unexpectedType;

import java.io.Serializable;

public class CallableUnexceptedTypeResult implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PREFIX = "REPLY: ";

    private final String message;

    public CallableUnexceptedTypeResult(final CallableUnexceptedTypeEntity callableUnexceptedTypeEntity) {
	message = PREFIX + callableUnexceptedTypeEntity.getMessage();
    }

    @Override
    public String toString() {
	return message;
    }

    public String getMessage() {
	return message;
    }

}
