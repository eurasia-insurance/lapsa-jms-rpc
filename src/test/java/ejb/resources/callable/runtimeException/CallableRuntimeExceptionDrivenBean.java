package ejb.resources.callable.runtimeException;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.service.JmsCallableServiceDrivenBean;

@MessageDriven(mappedName = CallableRuntimeExceptionDestination.GENERAL)
public class CallableRuntimeExceptionDrivenBean
	extends JmsCallableServiceDrivenBean<CallableRuntimeExceptionEntity, CallableRuntimeExceptionResult> {

    public CallableRuntimeExceptionDrivenBean() {
	super(CallableRuntimeExceptionEntity.class);
    }

    @Override
    public CallableRuntimeExceptionResult calling(CallableRuntimeExceptionEntity callableRuntimeExceptionEntity,
	    Properties properties) {
	throw new IllegalStateException();
    }

}
