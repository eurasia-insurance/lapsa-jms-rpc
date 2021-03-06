package test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.jms.JMSException;

import org.junit.Test;

import ejb.resources.callable.simple.CallableSimpleDestination;
import ejb.resources.callable.simple.CallableSimpleEntity;
import ejb.resources.callable.simple.CallableSimpleResult;
import tech.lapsa.lapsa.jmsRPC.client.JmsCallableClient;
import tech.lapsa.lapsa.jmsRPC.client.JmsDestination;
import tech.lapsa.lapsa.jmsRPC.client.JmsResultType;

public class CallableSimpleTest extends ArquillianBaseTestCase {

    @Inject
    @JmsDestination(CallableSimpleDestination.GENERAL)
    @JmsResultType(CallableSimpleResult.class)
    private JmsCallableClient<CallableSimpleEntity, CallableSimpleResult> callableClient;

    @Test
    public void basic() throws JMSException {
	{
	    final String MESSAGE = "Hello JMS world!";
	    final String EXPECTING_MESSAGE = CallableSimpleResult.PREFIX + MESSAGE;

	    final CallableSimpleEntity e = new CallableSimpleEntity(MESSAGE);
	    final CallableSimpleResult r = callableClient.call(e);
	    assertThat(r, not(nullValue()));
	    assertThat(r.getMessage(),
		    allOf(not(nullValue()), is(equalTo(EXPECTING_MESSAGE))));
	}
    }
}
