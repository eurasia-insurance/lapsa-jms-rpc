package ejb.resources.callable.nulls;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.javax.jms.ObjectFunctionDrivenBean;

@MessageDriven(mappedName = CallableNullsDestination.JNDI_NAME)
public class CallableNullsDrivenBean extends ObjectFunctionDrivenBean<CallableNullsEntity, CallableNullsResult> {

    public CallableNullsDrivenBean() {
	super(CallableNullsEntity.class);
    }

    @Override
    protected CallableNullsResult apply(CallableNullsEntity entity, Properties properties) {
	MyObjects.requireNull(entity);
	return null;
    }
}
