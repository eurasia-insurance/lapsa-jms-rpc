package test.ejb.resources.simpleTest;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.ObjectFunctionDrivenBean;

@MessageDriven(mappedName = SimpleTestDestination.JNDI_NAME)
public class SimpleTestDrivenBean extends ObjectFunctionDrivenBean<SimpleTestEntity, SimpleTestResult> {

    public SimpleTestDrivenBean() {
	super(SimpleTestEntity.class);
    }

    @Override
    protected SimpleTestResult apply(SimpleTestEntity simpleTestEntity, Properties properties) {
	return new SimpleTestResult(simpleTestEntity);
    }

}
