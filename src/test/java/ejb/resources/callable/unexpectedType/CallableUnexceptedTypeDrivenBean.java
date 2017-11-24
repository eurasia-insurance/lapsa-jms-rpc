package ejb.resources.callable.unexpectedType;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.CallableServiceDrivenBean;

@MessageDriven(mappedName = CallableUnexceptedTypeDestination.JNDI_NAME)
public class CallableUnexceptedTypeDrivenBean extends
	CallableServiceDrivenBean<CallableUnexceptedTypeEntity, CallableUnexceptedTypeResult> {

    public CallableUnexceptedTypeDrivenBean() {
	super(CallableUnexceptedTypeEntity.class);
    }

    @Override
    protected CallableUnexceptedTypeResult apply(CallableUnexceptedTypeEntity callableUnexceptedTypeEntity,
	    Properties properties) {
	return new CallableUnexceptedTypeResult(callableUnexceptedTypeEntity);
    }

}
