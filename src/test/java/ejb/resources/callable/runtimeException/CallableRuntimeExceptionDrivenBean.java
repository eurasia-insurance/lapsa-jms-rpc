package ejb.resources.callable.runtimeException;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.ObjectFunctionDrivenBean;

@MessageDriven(mappedName = CallableRuntimeExceptionDestination.JNDI_NAME)
public class CallableRuntimeExceptionDrivenBean
	extends ObjectFunctionDrivenBean<CallableRuntimeExceptionEntity, CallableRuntimeExceptionResult> {

    public CallableRuntimeExceptionDrivenBean() {
	super(CallableRuntimeExceptionEntity.class);
    }

    @Override
    protected CallableRuntimeExceptionResult apply(CallableRuntimeExceptionEntity callableRuntimeExceptionEntity,
	    Properties properties) {
	throw new IllegalStateException();
    }

}
