package test.ejb.resources.runtimeException;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.jms.Destination;

@Singleton
public class RuntimeExceptionTestDestination {

    public static final String JNDI_NAME = "test/runtimeExceptionTest";

    @Resource(name = JNDI_NAME)
    private Destination destination;

    public Destination getDestination() {
	return destination;
    }
}
