package test.ejb.resources.runtimeException;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.ObjectFunctionDrivenBean;

@MessageDriven(mappedName = RuntimeExceptionDestination.JNDI_NAME)
public class RuntimeExceptionDrivenBean extends ObjectFunctionDrivenBean<RuntimeExceptionEntity, RuntimeExceptionResult> {

    public RuntimeExceptionDrivenBean() {
	super(RuntimeExceptionEntity.class);
    }

    @Override
    protected RuntimeExceptionResult apply(RuntimeExceptionEntity runtimeExceptionEntity, Properties properties) {
	throw new NullPointerException();
    }

}
