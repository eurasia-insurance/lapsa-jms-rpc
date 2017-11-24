package ejb.resources.function.nulls;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.javax.jms.ObjectFunctionDrivenBean;

@MessageDriven(mappedName = FunctionNullsDestination.JNDI_NAME)
public class FunctionNullsDrivenBean extends ObjectFunctionDrivenBean<FunctionNullsEntity, FunctionNullsResult> {

    public FunctionNullsDrivenBean() {
	super(FunctionNullsEntity.class);
    }

    @Override
    protected FunctionNullsResult apply(FunctionNullsEntity entity, Properties properties) {
	MyObjects.requireNull(entity);
	return null;
    }
}
