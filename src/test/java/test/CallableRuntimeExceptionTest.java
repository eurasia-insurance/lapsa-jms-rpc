package test;

import javax.inject.Inject;

import org.junit.Test;

import ejb.resources.callable.runtimeException.CallableRuntimeExceptionDestination;
import ejb.resources.callable.runtimeException.CallableRuntimeExceptionEntity;
import ejb.resources.callable.runtimeException.CallableRuntimeExceptionResult;
import tech.lapsa.javax.jms.client.JmsCallableClient;
import tech.lapsa.javax.jms.client.JmsDestination;
import tech.lapsa.javax.jms.client.JmsEntityType;
import tech.lapsa.javax.jms.client.JmsResultType;
import test.assertion.Assertions;

public class CallableRuntimeExceptionTest extends ArquillianBaseTestCase {

    @Inject
    @JmsDestination(CallableRuntimeExceptionDestination.GENERAL)
    @JmsEntityType(CallableRuntimeExceptionEntity.class)
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
