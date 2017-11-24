package test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Properties;

import javax.inject.Inject;
import javax.jms.JMSException;

import org.junit.Test;

import ejb.resources.function.simple.FunctionSimpleDestination;
import ejb.resources.function.simple.FunctionSimpleDrivenBean;
import ejb.resources.function.simple.FunctionSimpleEntity;
import ejb.resources.function.simple.FunctionSimpleResult;
import tech.lapsa.javax.jms.MyJMSClient;
import tech.lapsa.javax.jms.MyJMSClient.MyJMSFunction;

public class FunctionSimpleTest extends ArquillianBaseTestCase {

    @Inject
    private MyJMSClient jmsClient;

    @Inject
    private FunctionSimpleDestination destination;

    @Test
    public void basic() throws JMSException {
	final MyJMSFunction<FunctionSimpleEntity, FunctionSimpleResult> service = jmsClient.createFunction(
		destination.getDestination(),
		FunctionSimpleResult.class);
	{
	    final String MESSAGE = "Hello JMS world!";
	    final String EXPECTING_MESSAGE = FunctionSimpleResult.PREFIX + MESSAGE;

	    final FunctionSimpleEntity e = new FunctionSimpleEntity(MESSAGE);
	    final FunctionSimpleResult r = service.apply(e);
	    assertThat(r, not(nullValue()));
	    assertThat(r.getMessage(),
		    allOf(not(nullValue()), is(equalTo(EXPECTING_MESSAGE))));
	}
    }

    @Test
    public void withProperties() throws JMSException {
	final MyJMSFunction<FunctionSimpleEntity, FunctionSimpleResult> service = jmsClient.createFunction(
		destination.getDestination(),
		FunctionSimpleResult.class);
	{
	    final String MESSAGE = "Hello, %1$s!";
	    final String NAME = "John Bull";
	    final String EXPECTING_MESSAGE = FunctionSimpleResult.PREFIX + "Hello, " + NAME + "!";

	    final Properties properties = new Properties();
	    properties.setProperty(FunctionSimpleDrivenBean.PROPERTY_NAME, NAME);

	    final FunctionSimpleEntity e = new FunctionSimpleEntity(MESSAGE);
	    final FunctionSimpleResult r = service.apply(e, properties);
	    assertThat(r, not(nullValue()));
	    assertThat(r.getMessage(),
		    allOf(not(nullValue()), is(equalTo(EXPECTING_MESSAGE))));
	}
    }
}
