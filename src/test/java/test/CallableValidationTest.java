package test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.validation.ValidationException;

import org.junit.Test;

import ejb.resources.function.simple.FunctionSimpleResult;
import ejb.resources.function.validation.FunctionValidationDestination;
import ejb.resources.function.validation.FunctionValidationEntity;
import ejb.resources.function.validation.FunctionValidationResult;
import tech.lapsa.javax.jms.JmsClient;
import tech.lapsa.javax.jms.JmsClient.JmsCallable;
import test.assertion.Assertions;

public class FunctionValidationTest extends ArquillianBaseTestCase {

    @Inject
    private JmsClient jmsClient;

    @Inject
    private FunctionValidationDestination destination;

    @Test
    public void validationOk() throws JMSException {
	final JmsCallable<FunctionValidationEntity, FunctionValidationResult> service = jmsClient.createCallable(
		destination.getDestination(),
		FunctionValidationResult.class);

	{
	    final String VALID_MESSAGE = "Hello JMS world!";
	    final FunctionValidationEntity e = new FunctionValidationEntity(VALID_MESSAGE);
	    final FunctionValidationResult r = service.call(e);
	    assertThat(r, not(nullValue()));
	    assertThat(r.getMessage(),
		    allOf(not(nullValue()), is(equalTo(FunctionSimpleResult.PREFIX + e.getMessage()))));
	}
    }

    @Test
    public void validationFail() throws Exception {
	final JmsCallable<FunctionValidationEntity, FunctionValidationResult> service = jmsClient.createCallable(
		destination.getDestination(),
		FunctionValidationResult.class);

	{
	    final String NULL_MESSAGE = null;
	    final FunctionValidationEntity e = new FunctionValidationEntity(NULL_MESSAGE);
	    Assertions.expectException(() -> service.call(e), ValidationException.class);
	}
    }
}
