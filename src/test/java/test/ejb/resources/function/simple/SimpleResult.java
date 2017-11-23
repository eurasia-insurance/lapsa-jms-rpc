package test.ejb.resources.function.simple;

import java.io.Serializable;

public class SimpleResult implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PREFIX = "REPLY: ";

    public SimpleResult(SimpleEntity simpleEntity) {
	this.message = PREFIX + simpleEntity.message;
    }

    @Override
    public String toString() {
	return message;
    }

    public final String message;

}
