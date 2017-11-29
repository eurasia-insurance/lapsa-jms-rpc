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
import tech.lapsa.javax.jms.JmsCallableResultType;
import tech.lapsa.javax.jms.JmsClientFactory.JmsCallable;
import tech.lapsa.javax.jms.JmsDestinationMappedName;
import tech.lapsa.javax.jms.JmsServiceEntityType;
import test.assertion.Assertions;

public class CallableValidationTest extends ArquillianBaseTestCase {

    @Inject
    @JmsDestinationMappedName(CallableValidationDestination.WITH_VALIDATION)
    @JmsServiceEntityType(CallableValidationEntity.class)
    @JmsCallableResultType(CallableValidationResult.class)
    private JmsCallable<CallableValidationEntity, CallableValidationResult> callable;

    @Test
    public void validationOk() throws JMSException {
	{
	    final String VALID_MESSAGE = "Hello JMS world!";
	    final CallableValidationEntity e = new CallableValidationEntity(VALID_MESSAGE);
	    final CallableValidationResult r = callable.call(e);
	    assertThat(r, not(nullValue()));
	    assertThat(r.getMessage(),
		    allOf(not(nullValue()), is(equalTo(CallableSimpleResult.PREFIX + e.getMessage()))));
	}
    }

    @Test
    public void validationFail() throws Exception {
	{
	    final String NULL_MESSAGE = null;
	    final CallableValidationEntity e = new CallableValidationEntity(NULL_MESSAGE);
	    Assertions.expectException(() -> callable.call(e), ValidationException.class);
	}
    }

    @Inject
    @JmsDestinationMappedName(CallableValidationDestination.SKIPPED_VALIDATION)
    @JmsServiceEntityType(CallableValidationEntity.class)
    @JmsCallableResultType(CallableValidationResult.class)
    private JmsCallable<CallableValidationEntity, CallableValidationResult> callable2;

    @Test
    public void validationNoFail() throws Exception {
	{
	    final String NULL_MESSAGE = null;
	    final CallableValidationEntity e = new CallableValidationEntity(NULL_MESSAGE);
	    final CallableValidationResult r = callable2.call(e);
	    assertThat(r, not(nullValue()));
	    assertThat(r.getMessage(),
		    allOf(not(nullValue()), is(equalTo(CallableSimpleResult.PREFIX + e.getMessage()))));
	}
    }

}
