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
    private FunctionUnexceptedTypeDestination functionUnexpectedTypeDestination;

    @Test
    public void unexpectedResult() throws Throwable {
	final MyJMSFunction<FunctionUnexceptedTypeEntity, FunctionUnexceptedTypeResult> function //
		= jmsClient.createFunction(functionUnexpectedTypeDestination.getDestinationUnexcpetedResult(),
			FunctionUnexceptedTypeResult.class);

	{
	    final String MESSAGE = "Hello JMS world!";
	    final FunctionUnexceptedTypeEntity e = new FunctionUnexceptedTypeEntity(MESSAGE);
	    Assertions.expectException(() -> function.apply(e), UnexpectedResponseTypeException.class);
	}
    }

    @Test
    public void unexpectedRequest() throws Throwable {
	final MyJMSFunction<FunctionUnexceptedTypeEntity_Unexpected, FunctionUnexceptedTypeResult> function //
		= jmsClient.createFunction(functionUnexpectedTypeDestination.getDestination(),
			FunctionUnexceptedTypeResult.class);

	{
	    final String MESSAGE = "Hello JMS world!";
	    final FunctionUnexceptedTypeEntity_Unexpected e = new FunctionUnexceptedTypeEntity_Unexpected(MESSAGE);
	    Assertions.expectException(() -> function.apply(e), UnexpectedTypeRequestedException.class);
	}
    }
}
