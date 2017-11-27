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

    public static final String JNDI_NAME_SKIP_VALIDATION = "test/callable/validation_skipValidation";

    @Resource(name = JNDI_NAME_SKIP_VALIDATION)
    private Destination destinationSkipValidation;

    public Destination getDestinationSkipValidation() {
	return destinationSkipValidation;
    }
}
