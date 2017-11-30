package test;

import javax.inject.Inject;

import org.junit.Test;

import ejb.resources.callable.unexpectedType.CallableUnexceptedTypeDestination;
import ejb.resources.callable.unexpectedType.CallableUnexceptedTypeEntity;
import ejb.resources.callable.unexpectedType.CallableUnexceptedTypeEntity_Unexpected;
import ejb.resources.callable.unexpectedType.CallableUnexceptedTypeResult;
import tech.lapsa.javax.jms.UnexpectedResponseTypeException;
import tech.lapsa.javax.jms.UnexpectedTypeRequestedException;
import tech.lapsa.javax.jms.client.JmsCallableClient;
import tech.lapsa.javax.jms.client.JmsDestination;
import tech.lapsa.javax.jms.client.JmsResultType;
import test.assertion.Assertions;

public class CallableUnexpectedTypeTest extends ArquillianBaseTestCase {

    @Inject
    @JmsDestination(CallableUnexceptedTypeDestination.UNEXPECTED_RESULT)
    @JmsResultType(CallableUnexceptedTypeResult.class)
    private JmsCallableClient<CallableUnexceptedTypeEntity, CallableUnexceptedTypeResult> callableClient1;

    @Test
    public void unexpectedResult() throws Throwable {
	{
	    final String MESSAGE = "Hello JMS world!";
	    final CallableUnexceptedTypeEntity e = new CallableUnexceptedTypeEntity(MESSAGE);
	    Assertions.expectException(() -> callableClient1.call(e), UnexpectedResponseTypeException.class);
	}
    }

    @Inject
    @JmsDestination(CallableUnexceptedTypeDestination.UNEXPECTED_ENTITY)
    @JmsResultType(CallableUnexceptedTypeResult.class)
    private JmsCallableClient<CallableUnexceptedTypeEntity_Unexpected, CallableUnexceptedTypeResult> callableClient2;

    @Test
    public void unexpectedRequest() throws Throwable {
	{
	    final String MESSAGE = "Hello JMS world!";
	    final CallableUnexceptedTypeEntity_Unexpected e = new CallableUnexceptedTypeEntity_Unexpected(MESSAGE);
	    Assertions.expectException(() -> callableClient2.call(e), UnexpectedTypeRequestedException.class);
	}
    }
}
