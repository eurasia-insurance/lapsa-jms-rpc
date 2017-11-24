package test;

import javax.inject.Inject;

import org.junit.Test;

import ejb.resources.function.unexpectedType.FunctionUnexceptedTypeDestination;
import ejb.resources.function.unexpectedType.FunctionUnexceptedTypeEntity;
import ejb.resources.function.unexpectedType.FunctionUnexceptedTypeEntity_Unexpected;
import ejb.resources.function.unexpectedType.FunctionUnexceptedTypeResult;
import tech.lapsa.javax.jms.JmsClient;
import tech.lapsa.javax.jms.JmsClient.JmsCallable;
import tech.lapsa.javax.jms.UnexpectedResponseTypeException;
import tech.lapsa.javax.jms.UnexpectedTypeRequestedException;
import test.assertion.Assertions;

public class FunctionUnexpectedTypeTest extends ArquillianBaseTestCase {

    @Inject
    private JmsClient jmsClient;

    @Inject
    private FunctionUnexceptedTypeDestination destination;

    @Test
    public void unexpectedResult() throws Throwable {
	final JmsCallable<FunctionUnexceptedTypeEntity, FunctionUnexceptedTypeResult> service //
		= jmsClient.createCallable(destination.getDestinationUnexcpetedResult(),
			FunctionUnexceptedTypeResult.class);

	{
	    final String MESSAGE = "Hello JMS world!";
	    final FunctionUnexceptedTypeEntity e = new FunctionUnexceptedTypeEntity(MESSAGE);
	    Assertions.expectException(() -> service.call(e), UnexpectedResponseTypeException.class);
	}
    }

    @Test
    public void unexpectedRequest() throws Throwable {
	final JmsCallable<FunctionUnexceptedTypeEntity_Unexpected, FunctionUnexceptedTypeResult> service //
		= jmsClient.createCallable(destination.getDestination(),
			FunctionUnexceptedTypeResult.class);

	{
	    final String MESSAGE = "Hello JMS world!";
	    final FunctionUnexceptedTypeEntity_Unexpected e = new FunctionUnexceptedTypeEntity_Unexpected(MESSAGE);
	    Assertions.expectException(() -> service.call(e), UnexpectedTypeRequestedException.class);
	}
    }
}
