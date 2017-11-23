package test.ejb.resources.simpletest;

import java.io.Serializable;

public class SimpleTestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public SimpleTestEntity(String message) {
	this.message = message;
    }
    
    @Override
    public String toString() {
        return message;
    }

    public final String message;
}
