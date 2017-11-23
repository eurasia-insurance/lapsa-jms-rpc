package test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.validation.ValidationException;

import org.junit.Test;

import tech.lapsa.javax.jms.MyJMSClient;
import tech.lapsa.javax.jms.MyJMSClient.MyJMSFunction;
import test.assertion.Assertions;
import test.ejb.resources.function.runtimeException.RuntimeExceptionDestination;
import test.ejb.resources.function.runtimeException.RuntimeExceptionEntity;
import test.ejb.resources.function.runtimeException.RuntimeExceptionResult;
import test.ejb.resources.function.simple.SimpleDestination;
import test.ejb.resources.function.simple.SimpleEntity;
import test.ejb.resources.function.simple.SimpleResult;
import test.ejb.resources.function.validation.ValidationDestination;
import test.ejb.resources.function.validation.ValidationEntity;
import test.ejb.resources.function.validation.ValidationResult;

public class ObjectFunctionTest extends ArquillianBaseTestCase {

    @Inject
    private MyJMSClient jmsClient;

    @Inject
    private SimpleDestination simpleDestination;

    @Test
    public void simpleTest_1() throws JMSException {
	final MyJMSFunction<SimpleEntity, SimpleResult> function = jmsClient.createFunction(
		simpleDestination.getDestination(),
		SimpleResult.class);
	{
	    final String MESSAGE = "Hello JMS world!";
	    final SimpleEntity e = new SimpleEntity(MESSAGE);
	    final SimpleResult r = function.apply(e);
	    assertThat(r, not(nullValue()));
	    assertThat(r.message, allOf(not(nullValue()), is(equalTo(SimpleResult.PREFIX + e.message))));
	}
    }

    @Inject
    private ValidationDestination validationDestination;

    @Test
    public void validationTest_1() throws JMSException {
	final MyJMSFunction<ValidationEntity, ValidationResult> function = jmsClient.createFunction(
		validationDestination.getDestination(),
		ValidationResult.class);

	{
	    final String VALID_MESSAGE = "Hello JMS world!";
	    final ValidationEntity e = new ValidationEntity(VALID_MESSAGE);
	    final ValidationResult r = function.apply(e);
	    assertThat(r, not(nullValue()));
	    assertThat(r.getMessage(), allOf(not(nullValue()), is(equalTo(SimpleResult.PREFIX + e.getMessage()))));
	}
    }

    @Test
    public void validationTest_2() {
	final MyJMSFunction<ValidationEntity, ValidationResult> function = jmsClient.createFunction(
		validationDestination.getDestination(),
		ValidationResult.class);

	{
	    final String NULL_MESSAGE = null;
	    final ValidationEntity e = new ValidationEntity(NULL_MESSAGE);
	    Assertions.expectException(() -> {
		try {
		    function.apply(e);
		} catch (JMSException e1) {
		    throw new RuntimeException(e1);
		}
	    }, ValidationException.class);
	}
    }

    @Inject
    private RuntimeExceptionDestination runtimeExceptionDestination;

    @Test
    public void runtimeExceptionTest_1() throws JMSException {
	final MyJMSFunction<RuntimeExceptionEntity, RuntimeExceptionResult> function = jmsClient.createFunction(
		runtimeExceptionDestination.getDestination(),
		RuntimeExceptionResult.class);

	{
	    final String MESSAGE = "Hello JMS world!";
	    final RuntimeExceptionEntity e = new RuntimeExceptionEntity(MESSAGE);
	    Assertions.expectException(() -> {
		try {
		    function.apply(e);
		} catch (JMSException e1) {
		    throw new RuntimeException(e1);
		}
	    }, NullPointerException.class);
	}
    }
}
