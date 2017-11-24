package ejb.resources.callable.validation;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.jms.Destination;

@Singleton
public class CallableValidationDestination {

    public static final String JNDI_NAME = "test/callable/validation";

    @Resource(name = JNDI_NAME)
    private Destination destination;

    public Destination getDestination() {
	return destination;
    }
}
