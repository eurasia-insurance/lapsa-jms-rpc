package test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.validation.ValidationException;

import org.junit.Test;

import ejb.resources.callable.simple.CallableSimpleResult;
import ejb.resources.callable.validation.CallableValidationDestination;
import ejb.resources.callable.validation.CallableValidationEntity;
import ejb.resources.callable.validation.CallableValidationResult;
import tech.lapsa.javax.jms.JmsClient;
import tech.lapsa.javax.jms.JmsClient.JmsCallable;
import test.assertion.Assertions;

public class CallableValidationTest extends ArquillianBaseTestCase {

    @Inject
    private JmsClient jmsClient;

    @Inject
    private CallableValidationDestination destination;

    @Test
    public void validationOk() throws JMSException {
	final JmsCallable<CallableValidationEntity, CallableValidationResult> service //
		= jmsClient.createCallable(destination.getDestination(), CallableValidationResult.class);

	{
	    final String VALID_MESSAGE = "Hello JMS world!";
	    final CallableValidationEntity e = new CallableValidationEntity(VALID_MESSAGE);
	    final CallableValidationResult r = service.call(e);
	    assertThat(r, not(nullValue()));
	    assertThat(r.getMessage(),
		    allOf(not(nullValue()), is(equalTo(CallableSimpleResult.PREFIX + e.getMessage()))));
	}
    }

    @Test
    public void validationFail() throws Exception {
	final JmsCallable<CallableValidationEntity, CallableValidationResult> service //
		= jmsClient.createCallable(destination.getDestination(), CallableValidationResult.class);

	{
	    final String NULL_MESSAGE = null;
	    final CallableValidationEntity e = new CallableValidationEntity(NULL_MESSAGE);
	    Assertions.expectException(() -> service.call(e), ValidationException.class);
	}
    }
}
