package ejb.resources.function.unexpectedType;

import java.io.Serializable;

public class FunctionUnexceptedTypeResult_Unexpected implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PREFIX = "REPLY: ";

    private final String message;

    public FunctionUnexceptedTypeResult_Unexpected(FunctionUnexceptedTypeEntity functionUnexceptedTypeEntity) {
	this.message = PREFIX + functionUnexceptedTypeEntity.getMessage();
    }

    @Override
    public String toString() {
	return message;
    }

    public String getMessage() {
	return message;
    }

}
