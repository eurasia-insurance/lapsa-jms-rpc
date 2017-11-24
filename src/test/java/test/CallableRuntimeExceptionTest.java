package test;

import javax.inject.Inject;

import org.junit.Test;

import ejb.resources.callable.runtimeException.CallableRuntimeExceptionDestination;
import ejb.resources.callable.runtimeException.CallableRuntimeExceptionEntity;
import ejb.resources.callable.runtimeException.CallableRuntimeExceptionResult;
import tech.lapsa.javax.jms.JmsClientFactory;
import tech.lapsa.javax.jms.JmsClientFactory.JmsCallable;
import test.assertion.Assertions;

public class CallableRuntimeExceptionTest extends ArquillianBaseTestCase {

    @Inject
    private JmsClientFactory jmsClientFactory;

    @Inject
    private CallableRuntimeExceptionDestination destination;

    @Test
    public void nullPointerException() throws Exception {
	final JmsCallable<CallableRuntimeExceptionEntity, CallableRuntimeExceptionResult> callable //
		= jmsClientFactory.createCallable(destination.getDestination(), CallableRuntimeExceptionResult.class);

	{
	    final String MESSAGE = "Hello JMS world!";
	    final CallableRuntimeExceptionEntity e = new CallableRuntimeExceptionEntity(MESSAGE);
	    Assertions.expectException(() -> callable.call(e), IllegalStateException.class);
	}
    }
}
