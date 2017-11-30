package ejb.resources.consumer.simple;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.javax.jms.service.JmsReceiverServiceDrivenBean;
import test.ConsumerSimpleTest;

@MessageDriven(mappedName = ConsumerSimpleDestination.JNDI_NAME)
public class ConsumerSimpleDrivenBean extends JmsReceiverServiceDrivenBean<ConsumerSimpleEntity> {

    public static final String PROPERTY_NAME = "name";

    public ConsumerSimpleDrivenBean() {
	super(ConsumerSimpleEntity.class);
    }

    @Override
    public void receiving(ConsumerSimpleEntity entity, Properties properties) {
	if (MyObjects.nonNull(properties)) {
	    final String name = properties.getProperty(PROPERTY_NAME);
	    if (MyStrings.nonEmpty(name))
		ConsumerSimpleTest.WITH_PROPERTIES_EXPECTED = name;
	}
	if (MyObjects.nonNull(entity))
	    ConsumerSimpleTest.BASIC_EXPECTED = entity;
    }
}
