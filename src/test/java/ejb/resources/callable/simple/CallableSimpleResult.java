package ejb.resources.function.simple;

import java.io.Serializable;

public class FunctionSimpleResult implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PREFIX = "REPLY: ";

    private final String message;
    private String name;

    public FunctionSimpleResult(FunctionSimpleEntity functionSimpleEntity) {
	this.message = PREFIX + functionSimpleEntity.getMessage();
    }

    @Override
    public String toString() {
	return message;
    }

    public String getMessage() {
	return String.format(message, name);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
