package test.ejb.resources.validationTest;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.ObjectFunctionDrivenBean;

@MessageDriven(mappedName = ValidationTestDestination.JNDI_NAME)
public class ValidationTestDrivenBean extends ObjectFunctionDrivenBean<ValidationTestEntity, ValidationTestResult> {

    public ValidationTestDrivenBean() {
	super(ValidationTestEntity.class);
    }

    @Override
    protected ValidationTestResult apply(ValidationTestEntity validationTestEntity, Properties properties) {
	return new ValidationTestResult(validationTestEntity);
    }

}
