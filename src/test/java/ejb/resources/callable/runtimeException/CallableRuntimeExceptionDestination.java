package ejb.resources.callable.runtimeException;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.jms.Destination;

@Singleton
public class CallableRuntimeExceptionDestination {

    public static final String JNDI_NAME = "test/callable/runtimeException";

    @Resource(name = JNDI_NAME)
    private Destination destination;

    public Destination getDestination() {
	return destination;
    }
}
