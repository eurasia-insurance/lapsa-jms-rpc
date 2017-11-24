package test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.jms.JMSException;

import org.junit.Test;

import ejb.resources.function.simple.FunctionSimpleDestination;
import ejb.resources.function.simple.FunctionSimpleEntity;
import ejb.resources.function.simple.FunctionSimpleResult;
import tech.lapsa.javax.jms.MyJMSClient;
import tech.lapsa.javax.jms.MyJMSClient.MyJMSFunction;

public class FunctionSimpleTest extends ArquillianBaseTestCase {

    @Inject
    private MyJMSClient jmsClient;

    @Inject
    private FunctionSimpleDestination functionSimpleDestination;

    @Test
    public void simple() throws JMSException {
	final MyJMSFunction<FunctionSimpleEntity, FunctionSimpleResult> function = jmsClient.createFunction(
		functionSimpleDestination.getDestination(),
		FunctionSimpleResult.class);
	{
	    final String MESSAGE = "Hello JMS world!";
	    final FunctionSimpleEntity e = new FunctionSimpleEntity(MESSAGE);
	    final FunctionSimpleResult r = function.apply(e);
	    assertThat(r, not(nullValue()));
	    assertThat(r.message, allOf(not(nullValue()), is(equalTo(FunctionSimpleResult.PREFIX + e.message))));
	}
    }
}
