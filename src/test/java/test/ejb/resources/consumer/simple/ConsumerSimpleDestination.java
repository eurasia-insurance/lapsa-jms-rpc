package test.ejb.resources.consumer.simple;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.jms.Destination;

@Singleton
public class ConsumerSimpleDestination {

    public static final String JNDI_NAME = "test/consumer/simple";

    @Resource(name = JNDI_NAME)
    private Destination destination;

    public Destination getDestination() {
	return destination;
    }
}
