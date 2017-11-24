package test;

import javax.inject.Inject;

import org.junit.Test;

import ejb.resources.function.runtimeException.FunctionRuntimeExceptionDestination;
import ejb.resources.function.runtimeException.FunctionRuntimeExceptionEntity;
import ejb.resources.function.runtimeException.FunctionRuntimeExceptionResult;
import tech.lapsa.javax.jms.MyJMSClient;
import tech.lapsa.javax.jms.MyJMSClient.MyJMSFunction;
import test.assertion.Assertions;

public class FunctionRuntimeExceptionTest extends ArquillianBaseTestCase {

    @Inject
    private MyJMSClient jmsClient;

    @Inject
    private FunctionRuntimeExceptionDestination destination;

    @Test
    public void nullPointerException() throws Exception {
	final MyJMSFunction<FunctionRuntimeExceptionEntity, FunctionRuntimeExceptionResult> service = jmsClient
		.createFunction(
			destination.getDestination(),
			FunctionRuntimeExceptionResult.class);

	{
	    final String MESSAGE = "Hello JMS world!";
	    final FunctionRuntimeExceptionEntity e = new FunctionRuntimeExceptionEntity(MESSAGE);
	    Assertions.expectException(() -> service.apply(e), IllegalStateException.class);
	}
    }
}
