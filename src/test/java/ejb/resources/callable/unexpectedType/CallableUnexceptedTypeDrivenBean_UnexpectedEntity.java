package ejb.resources.callable.unexpectedType;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.CallableServiceDrivenBean;

@MessageDriven(mappedName = CallableUnexceptedTypeDestination.UNEXPECTED_ENTITY)
public class CallableUnexceptedTypeDrivenBean_UnexpectedEntity extends
	CallableServiceDrivenBean<CallableUnexceptedTypeEntity, CallableUnexceptedTypeResult> {

    public CallableUnexceptedTypeDrivenBean_UnexpectedEntity() {
	super(CallableUnexceptedTypeEntity.class);
    }

    @Override
    public CallableUnexceptedTypeResult calling(CallableUnexceptedTypeEntity callableUnexceptedTypeEntity,
	    Properties properties) {
	return new CallableUnexceptedTypeResult(callableUnexceptedTypeEntity);
    }

}
