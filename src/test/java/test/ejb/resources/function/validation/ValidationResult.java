package test.ejb.resources.validation;

import java.io.Serializable;

public class ValidationResult implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PREFIX = "REPLY: ";

    private final String message;

    public ValidationResult(ValidationEntity validationEntity) {
	this.message = PREFIX + validationEntity.getMessage();
    }

    @Override
    public String toString() {
	return message;
    }

    public String getMessage() {
	return message;
    }

}
