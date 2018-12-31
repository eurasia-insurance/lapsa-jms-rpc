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
import tech.lapsa.lapsa.jmsRPC.client.JmsCallableClient;
import tech.lapsa.lapsa.jmsRPC.client.JmsDestination;
import tech.lapsa.lapsa.jmsRPC.client.JmsResultType;

public class CallableWithPropertiesTest extends ArquillianBaseTestCase {

    @Inject
    @JmsDestination(CallableSimpleDestination.GENERAL)
    @JmsResultType(CallableSimpleResult.class)
    private JmsCallableClient<CallableSimpleEntity, CallableSimpleResult> callableClient;

    @Test
    public void basic() throws JMSException {
	{
	    final String MESSAGE = "Hello, %1$s!";
	    final String NAME = "John Bull";
	    final String EXPECTING_MESSAGE = CallableSimpleResult.PREFIX + "Hello, " + NAME + "!";

	    final Properties properties = new Properties();
	    properties.setProperty(CallableSimpleDrivenBean.PROPERTY_NAME, NAME);

	    final CallableSimpleEntity e = new CallableSimpleEntity(MESSAGE);
	    final CallableSimpleResult r = callableClient.call(e, properties);
	    assertThat(r, not(nullValue()));
	    assertThat(r.getMessage(),
		    allOf(not(nullValue()), is(equalTo(EXPECTING_MESSAGE))));
	}
    }
}
