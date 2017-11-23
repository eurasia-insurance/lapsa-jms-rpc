package test.ejb.resources.function.validation;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.ObjectFunctionDrivenBean;

@MessageDriven(mappedName = ValidationDestination.JNDI_NAME)
public class ValidationDrivenBean extends ObjectFunctionDrivenBean<ValidationEntity, ValidationResult> {

    public ValidationDrivenBean() {
	super(ValidationEntity.class);
    }

    @Override
    protected ValidationResult apply(ValidationEntity validationEntity, Properties properties) {
	return new ValidationResult(validationEntity);
    }

}
