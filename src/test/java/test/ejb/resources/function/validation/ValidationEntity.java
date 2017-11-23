package test.ejb.resources.function.validation;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class ValidationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private final String message;

    public ValidationEntity(String message) {
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
