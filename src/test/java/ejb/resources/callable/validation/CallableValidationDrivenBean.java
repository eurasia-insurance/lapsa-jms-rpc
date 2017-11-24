package ejb.resources.function.validation;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.ObjectFunctionDrivenBean;

@MessageDriven(mappedName = FunctionValidationDestination.JNDI_NAME)
public class FunctionValidationDrivenBean extends ObjectFunctionDrivenBean<FunctionValidationEntity, FunctionValidationResult> {

    public FunctionValidationDrivenBean() {
	super(FunctionValidationEntity.class);
    }

    @Override
    protected FunctionValidationResult apply(FunctionValidationEntity functionValidationEntity, Properties properties) {
	return new FunctionValidationResult(functionValidationEntity);
    }

}
