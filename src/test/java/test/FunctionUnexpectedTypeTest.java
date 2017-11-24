package test;

import javax.inject.Inject;

import org.junit.Test;

import ejb.resources.function.unexpectedType.FunctionUnexceptedTypeDestination;
import ejb.resources.function.unexpectedType.FunctionUnexceptedTypeEntity;
import ejb.resources.function.unexpectedType.FunctionUnexceptedTypeEntity_Unexpected;
import ejb.resources.function.unexpectedType.FunctionUnexceptedTypeResult;
import tech.lapsa.javax.jms.MyJMSClient;
import tech.lapsa.javax.jms.MyJMSClient.MyJMSFunction;
import tech.lapsa.javax.jms.UnexpectedResponseTypeException;
import tech.lapsa.javax.jms.UnexpectedTypeRequestedException;
import test.assertion.Assertions;

public class FunctionUnexpectedTypeTest extends ArquillianBaseTestCase {

    @Inject
    private MyJMSClient jmsClient;

    @Inject
    private FunctionUnexceptedTypeDestination destination;

    @Test
    public void unexpectedResult() throws Throwable {
	final MyJMSFunction<FunctionUnexceptedTypeEntity, FunctionUnexceptedTypeResult> service //
		= jmsClient.createFunction(destination.getDestinationUnexcpetedResult(),
			FunctionUnexceptedTypeResult.class);

	{
	    final String MESSAGE = "Hello JMS world!";
	    final FunctionUnexceptedTypeEntity e = new FunctionUnexceptedTypeEntity(MESSAGE);
	    Assertions.expectException(() -> service.call(e), UnexpectedResponseTypeException.class);
	}
    }

    @Test
    public void unexpectedRequest() throws Throwable {
	final MyJMSFunction<FunctionUnexceptedTypeEntity_Unexpected, FunctionUnexceptedTypeResult> service //
		= jmsClient.createFunction(destination.getDestination(),
			FunctionUnexceptedTypeResult.class);

	{
	    final String MESSAGE = "Hello JMS world!";
	    final FunctionUnexceptedTypeEntity_Unexpected e = new FunctionUnexceptedTypeEntity_Unexpected(MESSAGE);
	    Assertions.expectException(() -> service.call(e), UnexpectedTypeRequestedException.class);
	}
    }
}
