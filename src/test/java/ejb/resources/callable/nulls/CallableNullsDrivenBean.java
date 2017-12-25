package ejb.resources.callable.nulls;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.lapsa.jmsRPC.service.JmsCallableServiceDrivenBean;

@MessageDriven(mappedName = CallableNullsDestination.GENERAL)
public class CallableNullsDrivenBean extends JmsCallableServiceDrivenBean<CallableNullsEntity, CallableNullsResult> {

    public CallableNullsDrivenBean() {
	super(CallableNullsEntity.class);
    }

    @Override
    public CallableNullsResult calling(final CallableNullsEntity entity, final Properties properties) {
	MyObjects.requireNull(entity);
	return null;
    }
}
