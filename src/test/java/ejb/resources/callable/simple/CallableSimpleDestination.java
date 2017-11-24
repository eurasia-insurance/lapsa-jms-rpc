package ejb.resources.callable.simple;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.jms.Destination;

@Singleton
public class CallableSimpleDestination {

    public static final String JNDI_NAME = "test/callable/simple";

    @Resource(name = JNDI_NAME)
    private Destination destination;

    public Destination getDestination() {
	return destination;
    }
}
