package ejb.resources.callable.unexpectedType;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.lapsa.jmsRPC.service.JmsCallableServiceDrivenBean;

@MessageDriven(mappedName = CallableUnexceptedTypeDestination.UNEXPECTED_ENTITY)
public class CallableUnexceptedTypeDrivenBean_UnexpectedEntity extends
	JmsCallableServiceDrivenBean<CallableUnexceptedTypeEntity, CallableUnexceptedTypeResult> {

    public CallableUnexceptedTypeDrivenBean_UnexpectedEntity() {
	super(CallableUnexceptedTypeEntity.class);
    }

    @Override
    public CallableUnexceptedTypeResult calling(final CallableUnexceptedTypeEntity callableUnexceptedTypeEntity,
	    final Properties properties) {
	return new CallableUnexceptedTypeResult(callableUnexceptedTypeEntity);
    }

}
