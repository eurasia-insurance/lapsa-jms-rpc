package ejb.resources.function.runtimeException;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.ObjectFunctionDrivenBean;

@MessageDriven(mappedName = FunctionRuntimeExceptionDestination.JNDI_NAME)
public class FunctionRuntimeExceptionDrivenBean extends ObjectFunctionDrivenBean<FunctionRuntimeExceptionEntity, FunctionRuntimeExceptionResult> {

    public FunctionRuntimeExceptionDrivenBean() {
	super(FunctionRuntimeExceptionEntity.class);
    }

    @Override
    protected FunctionRuntimeExceptionResult apply(FunctionRuntimeExceptionEntity functionRuntimeExceptionEntity, Properties properties) {
	throw new NullPointerException();
    }

}
