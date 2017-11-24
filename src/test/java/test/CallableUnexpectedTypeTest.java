package test;

import javax.inject.Inject;

import org.junit.Test;

import ejb.resources.callable.unexpectedType.CallableUnexceptedTypeDestination;
import ejb.resources.callable.unexpectedType.CallableUnexceptedTypeEntity;
import ejb.resources.callable.unexpectedType.CallableUnexceptedTypeEntity_Unexpected;
import ejb.resources.callable.unexpectedType.CallableUnexceptedTypeResult;
import tech.lapsa.javax.jms.JmsClientFactory;
import tech.lapsa.javax.jms.JmsClientFactory.JmsCallable;
import tech.lapsa.javax.jms.UnexpectedResponseTypeException;
import tech.lapsa.javax.jms.UnexpectedTypeRequestedException;
import test.assertion.Assertions;

public class CallableUnexpectedTypeTest extends ArquillianBaseTestCase {

    @Inject
    private JmsClientFactory jmsClientFactory;

    @Inject
    private CallableUnexceptedTypeDestination destination;

    @Test
    public void unexpectedResult() throws Throwable {
	final JmsCallable<CallableUnexceptedTypeEntity, CallableUnexceptedTypeResult> service //
		= jmsClientFactory.createCallable(destination.getDestinationUnexcpetedResult(),
			CallableUnexceptedTypeResult.class);

	{
	    final String MESSAGE = "Hello JMS world!";
	    final CallableUnexceptedTypeEntity e = new CallableUnexceptedTypeEntity(MESSAGE);
	    Assertions.expectException(() -> service.call(e), UnexpectedResponseTypeException.class);
	}
    }

    @Test
    public void unexpectedRequest() throws Throwable {
	final JmsCallable<CallableUnexceptedTypeEntity_Unexpected, CallableUnexceptedTypeResult> service //
		= jmsClientFactory.createCallable(destination.getDestination(),
			CallableUnexceptedTypeResult.class);

	{
	    final String MESSAGE = "Hello JMS world!";
	    final CallableUnexceptedTypeEntity_Unexpected e = new CallableUnexceptedTypeEntity_Unexpected(MESSAGE);
	    Assertions.expectException(() -> service.call(e), UnexpectedTypeRequestedException.class);
	}
    }
}