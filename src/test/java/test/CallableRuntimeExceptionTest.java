package test;

import javax.inject.Inject;

import org.junit.Test;

import ejb.resources.callable.runtimeException.CallableRuntimeExceptionDestination;
import ejb.resources.callable.runtimeException.CallableRuntimeExceptionEntity;
import ejb.resources.callable.runtimeException.CallableRuntimeExceptionResult;
import tech.lapsa.lapsa.jmsRPC.client.JmsCallableClient;
import tech.lapsa.lapsa.jmsRPC.client.JmsDestination;
import tech.lapsa.lapsa.jmsRPC.client.JmsResultType;
import test.assertion.Assertions;

public class CallableRuntimeExceptionTest extends ArquillianBaseTestCase {

    @Inject
    @JmsDestination(CallableRuntimeExceptionDestination.GENERAL)
    @JmsResultType(CallableRuntimeExceptionResult.class)
    private JmsCallableClient<CallableRuntimeExceptionEntity, CallableRuntimeExceptionResult> callableClient;

    @Test
    public void nullPointerException() throws Exception {
	{
	    final String MESSAGE = "Hello JMS world!";
	    final CallableRuntimeExceptionEntity e = new CallableRuntimeExceptionEntity(MESSAGE);
	    Assertions.expectException(() -> callableClient.call(e), IllegalStateException.class);
	}
    }
}
