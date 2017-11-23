package test.ejb.resources.function.simple;

import java.io.Serializable;

public class FunctionSimpleResult implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PREFIX = "REPLY: ";

    public FunctionSimpleResult(FunctionSimpleEntity functionSimpleEntity) {
	this.message = PREFIX + functionSimpleEntity.message;
    }

    @Override
    public String toString() {
	return message;
    }

    public final String message;

}
