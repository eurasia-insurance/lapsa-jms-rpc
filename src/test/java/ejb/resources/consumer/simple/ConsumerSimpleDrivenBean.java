package ejb.resources.consumer.simple;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.ObjectConsumerDrivenBean;
import test.ConsumerSimpleTest;

@MessageDriven(mappedName = ConsumerSimpleDestination.JNDI_NAME)
public class ConsumerSimpleDrivenBean extends ObjectConsumerDrivenBean<ConsumerSimpleEntity> {

    public ConsumerSimpleDrivenBean() {
	super(ConsumerSimpleEntity.class);
    }

    @Override
    protected void accept(ConsumerSimpleEntity entity, Properties properties) {
	ConsumerSimpleTest.BASIC_EXPECTED = entity;
    }

}
