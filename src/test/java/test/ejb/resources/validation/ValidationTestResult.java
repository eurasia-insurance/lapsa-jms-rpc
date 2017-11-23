package test.ejb.resources.validation;

import java.io.Serializable;

public class ValidationTestResult implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PREFIX = "REPLY: ";

    private final String message;

    public ValidationTestResult(ValidationTestEntity validationTestEntity) {
	this.message = PREFIX + validationTestEntity.getMessage();
    }

    @Override
    public String toString() {
	return message;
    }

    public String getMessage() {
	return message;
    }

}
