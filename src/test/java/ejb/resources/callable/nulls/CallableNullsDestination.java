package ejb.resources.callable.nulls;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.jms.Destination;

@Singleton
public class CallableNullsDestination {

    public static final String JNDI_NAME = "test/callable/nulls";

    @Resource(name = JNDI_NAME)
    private Destination destination;

    public Destination getDestination() {
	return destination;
    }
}
