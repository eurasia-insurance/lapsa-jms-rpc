package test.ejb.resources.simple;

import java.io.Serializable;

public class SimpleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public SimpleEntity(String message) {
	this.message = message;
    }
    
    @Override
    public String toString() {
        return message;
    }

    public final String message;
}
