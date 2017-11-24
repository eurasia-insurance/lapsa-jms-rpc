package ejb.resources.function.validation;

import java.io.Serializable;

public class FunctionValidationResult implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PREFIX = "REPLY: ";

    private final String message;

    public FunctionValidationResult(FunctionValidationEntity functionValidationEntity) {
	this.message = PREFIX + functionValidationEntity.getMessage();
    }

    @Override
    public String toString() {
	return message;
    }

    public String getMessage() {
	return message;
    }

}
