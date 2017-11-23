package test.ejb.resources.function.runtimeException;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.jms.Destination;

@Singleton
public class FunctionRuntimeExceptionDestination {

    public static final String JNDI_NAME = "test/function/runtimeException";

    @Resource(name = JNDI_NAME)
    private Destination destination;

    public Destination getDestination() {
	return destination;
    }
}
