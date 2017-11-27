package ejb.resources.callable.validation;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.CallableServiceDrivenBean;
import tech.lapsa.javax.jms.JmsSkipValidation;

@MessageDriven(mappedName = CallableValidationDestination.JNDI_NAME_SKIP_VALIDATION)
@JmsSkipValidation
public class CallableValidationDrivenBean_SkipValidation
	extends CallableServiceDrivenBean<CallableValidationEntity, CallableValidationResult> {

    public CallableValidationDrivenBean_SkipValidation() {
	super(CallableValidationEntity.class);
    }

    @Override
    public CallableValidationResult calling(CallableValidationEntity callableValidationEntity, Properties properties) {
	return new CallableValidationResult(callableValidationEntity);
    }

}
