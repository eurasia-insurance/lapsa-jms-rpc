package ejb.resources.function.nulls;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.jms.Destination;

@Singleton
public class FunctionNullsDestination {

    public static final String JNDI_NAME = "test/function/nulls";

    @Resource(name = JNDI_NAME)
    private Destination destination;

    public Destination getDestination() {
	return destination;
    }
}
