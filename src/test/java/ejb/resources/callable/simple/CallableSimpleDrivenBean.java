package ejb.resources.callable.simple;

import java.util.Properties;

import javax.ejb.MessageDriven;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.lapsa.jmsRPC.service.JmsCallableServiceDrivenBean;

@MessageDriven(mappedName = CallableSimpleDestination.GENERAL)
@TransactionManagement(TransactionManagementType.BEAN)
public class CallableSimpleDrivenBean extends JmsCallableServiceDrivenBean<CallableSimpleEntity, CallableSimpleResult> {

    public static final String PROPERTY_NAME = "name";

    public CallableSimpleDrivenBean() {
	super(CallableSimpleEntity.class);
    }

    @Override
    public CallableSimpleResult calling(final CallableSimpleEntity callableSimpleEntity, final Properties properties) {
	final CallableSimpleResult result = new CallableSimpleResult(callableSimpleEntity);
	if (properties != null) {
	    final String name = properties.getProperty(PROPERTY_NAME);
	    if (MyStrings.nonEmpty(name))
		result.setName(name);
	}
	return result;
    }
}
