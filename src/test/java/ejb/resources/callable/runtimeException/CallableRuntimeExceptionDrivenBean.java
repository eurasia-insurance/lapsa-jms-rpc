package ejb.resources.callable.runtimeException;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.CallableServiceDrivenBean;

@MessageDriven(mappedName = CallableRuntimeExceptionDestination.JNDI_NAME)
public class CallableRuntimeExceptionDrivenBean
	extends CallableServiceDrivenBean<CallableRuntimeExceptionEntity, CallableRuntimeExceptionResult> {

    public CallableRuntimeExceptionDrivenBean() {
	super(CallableRuntimeExceptionEntity.class);
    }

    @Override
    protected CallableRuntimeExceptionResult apply(CallableRuntimeExceptionEntity callableRuntimeExceptionEntity,
	    Properties properties) {
	throw new IllegalStateException();
    }

}
