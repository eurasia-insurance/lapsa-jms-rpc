package test;

import javax.inject.Inject;

import org.junit.Test;

import ejb.resources.function.runtimeException.FunctionRuntimeExceptionDestination;
import ejb.resources.function.runtimeException.FunctionRuntimeExceptionEntity;
import ejb.resources.function.runtimeException.FunctionRuntimeExceptionResult;
import tech.lapsa.javax.jms.JmsClient;
import tech.lapsa.javax.jms.JmsClient.JmsCallable;
import test.assertion.Assertions;

public class FunctionRuntimeExceptionTest extends ArquillianBaseTestCase {

    @Inject
    private JmsClient jmsClient;

    @Inject
    private FunctionRuntimeExceptionDestination destination;

    @Test
    public void nullPointerException() throws Exception {
	final JmsCallable<FunctionRuntimeExceptionEntity, FunctionRuntimeExceptionResult> service = jmsClient
		.createCallable(
			destination.getDestination(),
			FunctionRuntimeExceptionResult.class);

	{
	    final String MESSAGE = "Hello JMS world!";
	    final FunctionRuntimeExceptionEntity e = new FunctionRuntimeExceptionEntity(MESSAGE);
	    Assertions.expectException(() -> service.call(e), IllegalStateException.class);
	}
    }
}
