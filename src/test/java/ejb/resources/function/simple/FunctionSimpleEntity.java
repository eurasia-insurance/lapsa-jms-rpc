package ejb.resources.function.simple;

import java.io.Serializable;

public class FunctionSimpleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;

    public FunctionSimpleEntity(String message) {
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
