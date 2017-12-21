package ejb.resources.callable.validation;

import java.io.Serializable;

public class CallableValidationResult implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PREFIX = "REPLY: ";

    private final String message;

    public CallableValidationResult(final CallableValidationEntity callableValidationEntity) {
	message = PREFIX + callableValidationEntity.getMessage();
    }

    @Override
    public String toString() {
	return message;
    }

    public String getMessage() {
	return message;
    }

}
