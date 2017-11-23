package test.ejb.resources.simple;

import java.util.Properties;

import javax.ejb.MessageDriven;

import tech.lapsa.javax.jms.ObjectFunctionDrivenBean;

@MessageDriven(mappedName = SimpleDestination.JNDI_NAME)
public class SimpleDrivenBean extends ObjectFunctionDrivenBean<SimpleEntity, SimpleResult> {

    public SimpleDrivenBean() {
	super(SimpleEntity.class);
    }

    @Override
    protected SimpleResult apply(SimpleEntity simpleEntity, Properties properties) {
	return new SimpleResult(simpleEntity);
    }

}
