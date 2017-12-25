package ejb.resources.callable.runtimeException;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.lapsa.jmsRPC.service.JmsCallableServiceDrivenBean;

@MessageDriven(mappedName = CallableRuntimeExceptionDestination.GENERAL)
public class CallableRuntimeExceptionDrivenBean
	extends JmsCallableServiceDrivenBean<CallableRuntimeExceptionEntity, CallableRuntimeExceptionResult> {

    public CallableRuntimeExceptionDrivenBean() {
	super(CallableRuntimeExceptionEntity.class);
    }

    @Override
    public CallableRuntimeExceptionResult calling(final CallableRuntimeExceptionEntity callableRuntimeExceptionEntity,
	    final Properties properties) {
	throw new IllegalStateException();
    }

}
