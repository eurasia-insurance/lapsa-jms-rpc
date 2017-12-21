package ejb.resources.callable.unexpectedType;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.service.JmsCallableServiceDrivenBean;

@MessageDriven(mappedName = CallableUnexceptedTypeDestination.UNEXPECTED_RESULT)
public class CallableUnexceptedTypeDrivenBean_UnexpectedResult extends
	JmsCallableServiceDrivenBean<CallableUnexceptedTypeEntity, CallableUnexceptedTypeResult_Unexpected> {

    public CallableUnexceptedTypeDrivenBean_UnexpectedResult() {
	super(CallableUnexceptedTypeEntity.class);
    }

    @Override
    public CallableUnexceptedTypeResult_Unexpected calling(
	    final CallableUnexceptedTypeEntity callableUnexceptedTypeEntity, final Properties properties) {
	return new CallableUnexceptedTypeResult_Unexpected(callableUnexceptedTypeEntity);
    }

}
