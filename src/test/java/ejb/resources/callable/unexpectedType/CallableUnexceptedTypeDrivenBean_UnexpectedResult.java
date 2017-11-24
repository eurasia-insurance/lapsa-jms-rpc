package ejb.resources.callable.unexpectedType;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.ObjectFunctionDrivenBean;

@MessageDriven(mappedName = CallableUnexceptedTypeDestination.JNDI_NAME_UNEXPECTED_RESULT)
public class CallableUnexceptedTypeDrivenBean_UnexpectedResult extends
	ObjectFunctionDrivenBean<CallableUnexceptedTypeEntity, CallableUnexceptedTypeResult_Unexpected> {

    public CallableUnexceptedTypeDrivenBean_UnexpectedResult() {
	super(CallableUnexceptedTypeEntity.class);
    }

    @Override
    protected CallableUnexceptedTypeResult_Unexpected apply(
	    CallableUnexceptedTypeEntity callableUnexceptedTypeEntity, Properties properties) {
	return new CallableUnexceptedTypeResult_Unexpected(callableUnexceptedTypeEntity);
    }

}
