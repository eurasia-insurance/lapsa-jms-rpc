package test.ejb.resources.runtimeExceptionTest;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.ObjectFunctionDrivenBean;

@MessageDriven(mappedName = RuntimeExceptionTestDestination.JNDI_NAME)
public class RuntimeExceptionTestDrivenBean extends ObjectFunctionDrivenBean<RuntimeExceptionTestEntity, RuntimeExceptionTestResult> {

    public RuntimeExceptionTestDrivenBean() {
	super(RuntimeExceptionTestEntity.class);
    }

    @Override
    protected RuntimeExceptionTestResult apply(RuntimeExceptionTestEntity runtimeExceptionTestEntity, Properties properties) {
	throw new NullPointerException();
    }

}
