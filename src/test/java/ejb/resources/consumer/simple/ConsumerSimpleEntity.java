package ejb.resources.consumer.simple;

import java.io.Serializable;

public class ConsumerSimpleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public ConsumerSimpleEntity(String message) {
	this.message = message;
    }

    @Override
    public String toString() {
	return message;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null)
	    return false;
	if (obj == this)
	    return true;
	if (!(obj instanceof ConsumerSimpleEntity))
	    return false;
	final ConsumerSimpleEntity othr = (ConsumerSimpleEntity) obj;
	if (othr.message == this.message)
	    return true;
	if (message != null && message.equals(othr.message))
	    return true;
	return false;
    }

    @Override
    public int hashCode() {
	return 13 * (message != null //
		? message.hashCode() //
		: 1);
    }

    public final String message;
}
