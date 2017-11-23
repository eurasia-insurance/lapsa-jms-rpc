package test.ejb.resources.function.simple;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.ObjectFunctionDrivenBean;

@MessageDriven(mappedName = FunctionSimpleDestination.JNDI_NAME)
public class FunctionSimpleDrivenBean extends ObjectFunctionDrivenBean<FunctionSimpleEntity, FunctionSimpleResult> {

    public FunctionSimpleDrivenBean() {
	super(FunctionSimpleEntity.class);
    }

    @Override
    protected FunctionSimpleResult apply(FunctionSimpleEntity functionSimpleEntity, Properties properties) {
	return new FunctionSimpleResult(functionSimpleEntity);
    }

}
