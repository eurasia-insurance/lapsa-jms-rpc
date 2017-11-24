package ejb.resources.callable.simple;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.javax.jms.ObjectFunctionDrivenBean;

@MessageDriven(mappedName = CallableSimpleDestination.JNDI_NAME)
public class CallableSimpleDrivenBean extends ObjectFunctionDrivenBean<CallableSimpleEntity, CallableSimpleResult> {

    public static final String PROPERTY_NAME = "name";
    
    public CallableSimpleDrivenBean() {
	super(CallableSimpleEntity.class);
    }

    @Override
    protected CallableSimpleResult apply(CallableSimpleEntity callableSimpleEntity, Properties properties) {
	CallableSimpleResult result = new CallableSimpleResult(callableSimpleEntity);
	if (properties != null) {
	    final String name = properties.getProperty(PROPERTY_NAME);
	    if (MyStrings.nonEmpty(name))
		result.setName(name);
	}
	return result;
    }
}
