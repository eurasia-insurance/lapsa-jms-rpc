package ejb.resources.function.simple;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.javax.jms.ObjectFunctionDrivenBean;

@MessageDriven(mappedName = FunctionSimpleDestination.JNDI_NAME)
public class FunctionSimpleDrivenBean extends ObjectFunctionDrivenBean<FunctionSimpleEntity, FunctionSimpleResult> {

    public static final String PROPERTY_NAME = "name";
    
    public FunctionSimpleDrivenBean() {
	super(FunctionSimpleEntity.class);
    }

    @Override
    protected FunctionSimpleResult apply(FunctionSimpleEntity functionSimpleEntity, Properties properties) {
	FunctionSimpleResult result = new FunctionSimpleResult(functionSimpleEntity);
	if (properties != null) {
	    final String name = properties.getProperty(PROPERTY_NAME);
	    if (MyStrings.nonEmpty(name))
		result.setName(name);
	}
	return result;
    }
}
