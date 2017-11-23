package ejb.resources.consumer.simple;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.ObjectConsumerDrivenBean;

@MessageDriven(mappedName = ConsumerSimpleDestination.JNDI_NAME)
public class ConsumerSimpleDrivenBean extends ObjectConsumerDrivenBean<SimpleEntity> {

    public ConsumerSimpleDrivenBean() {
	super(SimpleEntity.class);
    }

    public static SimpleEntity lastReceived;

    @Override
    protected void accept(SimpleEntity entity, Properties properties) {
	ConsumerSimpleDrivenBean.lastReceived = entity;
    }

}
