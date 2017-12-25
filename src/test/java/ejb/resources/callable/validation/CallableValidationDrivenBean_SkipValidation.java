package ejb.resources.callable.validation;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.lapsa.jmsRPC.service.JmsCallableServiceDrivenBean;
import tech.lapsa.lapsa.jmsRPC.service.JmsSkipValidation;

@MessageDriven(mappedName = CallableValidationDestination.SKIPPED_VALIDATION)
@JmsSkipValidation
public class CallableValidationDrivenBean_SkipValidation
	extends JmsCallableServiceDrivenBean<CallableValidationEntity, CallableValidationResult> {

    public CallableValidationDrivenBean_SkipValidation() {
	super(CallableValidationEntity.class);
    }

    @Override
    public CallableValidationResult calling(final CallableValidationEntity callableValidationEntity,
	    final Properties properties) {
	return new CallableValidationResult(callableValidationEntity);
    }

}
