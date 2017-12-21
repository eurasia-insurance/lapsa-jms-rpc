package ejb.resources.callable.validation;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.service.JmsCallableServiceDrivenBean;

@MessageDriven(mappedName = CallableValidationDestination.WITH_VALIDATION)
public class CallableValidationDrivenBean
	extends JmsCallableServiceDrivenBean<CallableValidationEntity, CallableValidationResult> {

    public CallableValidationDrivenBean() {
	super(CallableValidationEntity.class);
    }

    @Override
    public CallableValidationResult calling(final CallableValidationEntity callableValidationEntity,
	    final Properties properties) {
	return new CallableValidationResult(callableValidationEntity);
    }

}
