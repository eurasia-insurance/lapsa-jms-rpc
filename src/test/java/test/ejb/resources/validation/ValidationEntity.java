package test.ejb.resources.validation;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class ValidationTestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private final String message;

    public ValidationTestEntity(String message) {
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
