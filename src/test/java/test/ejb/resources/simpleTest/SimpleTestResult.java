package test.ejb.resources.simpleTest;

import java.io.Serializable;

public class SimpleTestResult implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PREFIX = "REPLY: ";

    public SimpleTestResult(SimpleTestEntity simpleTestEntity) {
	this.message = PREFIX + simpleTestEntity.message;
    }

    @Override
    public String toString() {
	return message;
    }

    public final String message;

}
