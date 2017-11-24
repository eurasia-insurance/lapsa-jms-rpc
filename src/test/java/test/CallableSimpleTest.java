package test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Properties;

import javax.inject.Inject;
import javax.jms.JMSException;

import org.junit.Test;

import ejb.resources.callable.simple.CallableSimpleDestination;
import ejb.resources.callable.simple.CallableSimpleDrivenBean;
import ejb.resources.callable.simple.CallableSimpleEntity;
import ejb.resources.callable.simple.CallableSimpleResult;
import tech.lapsa.javax.jms.JmsClientFactory;
import tech.lapsa.javax.jms.JmsClientFactory.JmsCallable;

public class CallableSimpleTest extends ArquillianBaseTestCase {

    @Inject
    private JmsClientFactory jmsClientFactory;

    @Inject
    private CallableSimpleDestination destination;

    @Test
    public void basic() throws JMSException {
	final JmsCallable<CallableSimpleEntity, CallableSimpleResult> callable //
		= jmsClientFactory.createCallable(destination.getDestination(), CallableSimpleResult.class);
	{
	    final String MESSAGE = "Hello JMS world!";
	    final String EXPECTING_MESSAGE = CallableSimpleResult.PREFIX + MESSAGE;

	    final CallableSimpleEntity e = new CallableSimpleEntity(MESSAGE);
	    final CallableSimpleResult r = callable.call(e);
	    assertThat(r, not(nullValue()));
	    assertThat(r.getMessage(),
		    allOf(not(nullValue()), is(equalTo(EXPECTING_MESSAGE))));
	}
    }

    @Test
    public void withProperties() throws JMSException {
	final JmsCallable<CallableSimpleEntity, CallableSimpleResult> callable //
		= jmsClientFactory.createCallable(destination.getDestination(), CallableSimpleResult.class);
	{
	    final String MESSAGE = "Hello, %1$s!";
	    final String NAME = "John Bull";
	    final String EXPECTING_MESSAGE = CallableSimpleResult.PREFIX + "Hello, " + NAME + "!";

	    final Properties properties = new Properties();
	    properties.setProperty(CallableSimpleDrivenBean.PROPERTY_NAME, NAME);

	    final CallableSimpleEntity e = new CallableSimpleEntity(MESSAGE);
	    final CallableSimpleResult r = callable.call(e, properties);
	    assertThat(r, not(nullValue()));
	    assertThat(r.getMessage(),
		    allOf(not(nullValue()), is(equalTo(EXPECTING_MESSAGE))));
	}
    }
}
