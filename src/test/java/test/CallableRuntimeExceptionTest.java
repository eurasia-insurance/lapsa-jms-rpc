package test;

import javax.inject.Inject;

import org.junit.Test;

import ejb.resources.callable.runtimeException.CallableRuntimeExceptionDestination;
import ejb.resources.callable.runtimeException.CallableRuntimeExceptionEntity;
import ejb.resources.callable.runtimeException.CallableRuntimeExceptionResult;
import tech.lapsa.javax.jms.JmsClient;
import tech.lapsa.javax.jms.JmsClient.JmsCallable;
import test.assertion.Assertions;

public class CallableRuntimeExceptionTest extends ArquillianBaseTestCase {

    @Inject
    private JmsClient jmsClient;

    @Inject
    private CallableRuntimeExceptionDestination destination;

    @Test
    public void nullPointerException() throws Exception {
	final JmsCallable<CallableRuntimeExceptionEntity, CallableRuntimeExceptionResult> callable //
		= jmsClient.createCallable(destination.getDestination(), CallableRuntimeExceptionResult.class);

	{
	    final String MESSAGE = "Hello JMS world!";
	    final CallableRuntimeExceptionEntity e = new CallableRuntimeExceptionEntity(MESSAGE);
	    Assertions.expectException(() -> callable.call(e), IllegalStateException.class);
	}
    }
}
