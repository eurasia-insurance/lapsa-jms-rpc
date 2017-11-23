package test.ejb.resources.function.simple;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.jms.Destination;

@Singleton
public class FunctionSimpleDestination {

    public static final String JNDI_NAME = "test/function/simple";

    @Resource(name = JNDI_NAME)
    private Destination destination;

    public Destination getDestination() {
	return destination;
    }
}
