package ejb.resources.function.unexpectedType;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.jms.Destination;

@Singleton
public class FunctionUnexceptedTypeDestination {

    public static final String JNDI_NAME = "test/function/unexpectedType";

    @Resource(name = JNDI_NAME)
    private Destination destination;

    public Destination getDestination() {
	return destination;
    }

    public static final String JNDI_NAME_UNEXPECTED_RESULT = "test/function/unexpectedType_unexpectedResult";

    @Resource(name = JNDI_NAME_UNEXPECTED_RESULT)
    private Destination destinationUnexcpetedResult;

    public Destination getDestinationUnexcpetedResult() {
	return destinationUnexcpetedResult;
    }
}
