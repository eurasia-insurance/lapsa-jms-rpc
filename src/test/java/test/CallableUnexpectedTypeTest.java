package test;

import javax.inject.Inject;

import org.junit.Test;

import ejb.resources.callable.unexpectedType.CallableUnexceptedTypeDestination;
import ejb.resources.callable.unexpectedType.CallableUnexceptedTypeEntity;
import ejb.resources.callable.unexpectedType.CallableUnexceptedTypeEntity_Unexpected;
import ejb.resources.callable.unexpectedType.CallableUnexceptedTypeResult;
import tech.lapsa.javax.jms.JmsCallableResultType;
import tech.lapsa.javax.jms.JmsClientFactory.JmsCallable;
import tech.lapsa.javax.jms.JmsDestinationMappedName;
import tech.lapsa.javax.jms.JmsServiceEntityType;
import tech.lapsa.javax.jms.UnexpectedResponseTypeException;
import tech.lapsa.javax.jms.UnexpectedTypeRequestedException;
import test.assertion.Assertions;

public class CallableUnexpectedTypeTest extends ArquillianBaseTestCase {

    @Inject
    @JmsDestinationMappedName(CallableUnexceptedTypeDestination.UNEXPECTED_RESULT)
    @JmsServiceEntityType(CallableUnexceptedTypeEntity.class)
    @JmsCallableResultType(CallableUnexceptedTypeResult.class)
    private JmsCallable<CallableUnexceptedTypeEntity, CallableUnexceptedTypeResult> service1;

    @Test
    public void unexpectedResult() throws Throwable {
	{
	    final String MESSAGE = "Hello JMS world!";
	    final CallableUnexceptedTypeEntity e = new CallableUnexceptedTypeEntity(MESSAGE);
	    Assertions.expectException(() -> service1.call(e), UnexpectedResponseTypeException.class);
	}
    }

    @Inject
    @JmsDestinationMappedName(CallableUnexceptedTypeDestination.UNEXPECTED_ENTITY)
    @JmsServiceEntityType(CallableUnexceptedTypeEntity_Unexpected.class)
    @JmsCallableResultType(CallableUnexceptedTypeResult.class)
    private JmsCallable<CallableUnexceptedTypeEntity_Unexpected, CallableUnexceptedTypeResult> service2;

    @Test
    public void unexpectedRequest() throws Throwable {
	{
	    final String MESSAGE = "Hello JMS world!";
	    final CallableUnexceptedTypeEntity_Unexpected e = new CallableUnexceptedTypeEntity_Unexpected(MESSAGE);
	    Assertions.expectException(() -> service2.call(e), UnexpectedTypeRequestedException.class);
	}
    }
}
