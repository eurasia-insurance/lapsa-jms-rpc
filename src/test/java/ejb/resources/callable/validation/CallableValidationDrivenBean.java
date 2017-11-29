package ejb.resources.callable.validation;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.CallableServiceDrivenBean;

@MessageDriven(mappedName = CallableValidationDestination.WITH_VALIDATION)
public class CallableValidationDrivenBean
	extends CallableServiceDrivenBean<CallableValidationEntity, CallableValidationResult> {

    public CallableValidationDrivenBean() {
	super(CallableValidationEntity.class);
    }

    @Override
    public CallableValidationResult calling(CallableValidationEntity callableValidationEntity,
	    Properties properties) {
	return new CallableValidationResult(callableValidationEntity);
    }

}
