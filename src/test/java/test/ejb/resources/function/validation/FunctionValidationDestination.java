package test.ejb.resources.function.validation;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.jms.Destination;

@Singleton
public class FunctionValidationDestination {

    public static final String JNDI_NAME = "test/function/validation";

    @Resource(name = JNDI_NAME)
    private Destination destination;

    public Destination getDestination() {
	return destination;
    }
}
