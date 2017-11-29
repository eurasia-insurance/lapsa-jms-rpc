package test;

import javax.inject.Inject;

import org.junit.Test;

import ejb.resources.callable.runtimeException.CallableRuntimeExceptionDestination;
import ejb.resources.callable.runtimeException.CallableRuntimeExceptionEntity;
import ejb.resources.callable.runtimeException.CallableRuntimeExceptionResult;
import tech.lapsa.javax.jms.JmsCallableResultType;
import tech.lapsa.javax.jms.JmsClientFactory.JmsCallable;
import tech.lapsa.javax.jms.JmsDestinationMappedName;
import tech.lapsa.javax.jms.JmsServiceEntityType;
import test.assertion.Assertions;

public class CallableRuntimeExceptionTest extends ArquillianBaseTestCase {

    @Inject
    @JmsDestinationMappedName(CallableRuntimeExceptionDestination.GENERAL)
    @JmsServiceEntityType(CallableRuntimeExceptionEntity.class)
    @JmsCallableResultType(CallableRuntimeExceptionResult.class)
    private JmsCallable<CallableRuntimeExceptionEntity, CallableRuntimeExceptionResult> callable;

    @Test
    public void nullPointerException() throws Exception {
	{
	    final String MESSAGE = "Hello JMS world!";
	    final CallableRuntimeExceptionEntity e = new CallableRuntimeExceptionEntity(MESSAGE);
	    Assertions.expectException(() -> callable.call(e), IllegalStateException.class);
	}
    }
}
