package ejb.resources.function.unexpectedType;

import java.io.Serializable;

public class FunctionUnexceptedTypeEntity_Unexpected implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;

    public FunctionUnexceptedTypeEntity_Unexpected(String message) {
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
