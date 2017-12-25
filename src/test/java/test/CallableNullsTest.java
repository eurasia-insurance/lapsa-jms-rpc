package test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.jms.JMSException;

import org.junit.Test;

import ejb.resources.callable.nulls.CallableNullsDestination;
import ejb.resources.callable.nulls.CallableNullsEntity;
import ejb.resources.callable.nulls.CallableNullsResult;
import tech.lapsa.lapsa.jmsRPC.client.JmsCallableClient;
import tech.lapsa.lapsa.jmsRPC.client.JmsDestination;
import tech.lapsa.lapsa.jmsRPC.client.JmsResultType;

public class CallableNullsTest extends ArquillianBaseTestCase {

    @Inject
    @JmsDestination(CallableNullsDestination.GENERAL)
    @JmsResultType(CallableNullsResult.class)
    private JmsCallableClient<CallableNullsEntity, CallableNullsResult> callableClient;

    @Test
    public void basic() throws JMSException {
	{
	    final CallableNullsResult r = callableClient.call(null);
	    assertThat(r, nullValue());
	}
    }
}
