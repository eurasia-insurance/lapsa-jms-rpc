package ejb.resources.function.unexpectedType;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.ObjectFunctionDrivenBean;

@MessageDriven(mappedName = FunctionUnexceptedTypeDestination.JNDI_NAME)
public class FunctionUnexceptedTypeDrivenBean extends
	ObjectFunctionDrivenBean<FunctionUnexceptedTypeEntity, FunctionUnexceptedTypeResult> {

    public FunctionUnexceptedTypeDrivenBean() {
	super(FunctionUnexceptedTypeEntity.class);
    }

    @Override
    protected FunctionUnexceptedTypeResult apply(FunctionUnexceptedTypeEntity functionUnexceptedTypeEntity,
	    Properties properties) {
	return new FunctionUnexceptedTypeResult(functionUnexceptedTypeEntity);
    }

}
