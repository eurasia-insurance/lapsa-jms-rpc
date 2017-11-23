package test.ejb.resources.validation;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.jms.Destination;

@Singleton
public class ValidationTestDestination {

    public static final String JNDI_NAME = "test/validationTest";

    @Resource(name = JNDI_NAME)
    private Destination destination;

    public Destination getDestination() {
	return destination;
    }
}
