package ejb.resources.function.unexpectedType;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.ObjectFunctionDrivenBean;

@MessageDriven(mappedName = FunctionUnexceptedTypeDestination.JNDI_NAME_UNEXPECTED_RESULT)
public class FunctionUnexceptedTypeDrivenBean_UnexpectedResult extends
	ObjectFunctionDrivenBean<FunctionUnexceptedTypeEntity, FunctionUnexceptedTypeResult_Unexpected> {

    public FunctionUnexceptedTypeDrivenBean_UnexpectedResult() {
	super(FunctionUnexceptedTypeEntity.class);
    }

    @Override
    protected FunctionUnexceptedTypeResult_Unexpected apply(
	    FunctionUnexceptedTypeEntity functionUnexceptedTypeEntity, Properties properties) {
	return new FunctionUnexceptedTypeResult_Unexpected(functionUnexceptedTypeEntity);
    }

}
