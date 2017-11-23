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
import test.ejb.resources.runtimeExceptionTest.RuntimeExceptionTestDestination;
import test.ejb.resources.runtimeExceptionTest.RuntimeExceptionTestEntity;
import test.ejb.resources.runtimeExceptionTest.RuntimeExceptionTestResult;
import test.ejb.resources.simpleTest.SimpleTestDestination;
import test.ejb.resources.simpleTest.SimpleTestEntity;
import test.ejb.resources.simpleTest.SimpleTestResult;
import test.ejb.resources.validationTest.ValidationTestDestination;
import test.ejb.resources.validationTest.ValidationTestEntity;
import test.ejb.resources.validationTest.ValidationTestResult;

public class ObjectFunctionTest extends ArquillianBaseTestCase {

    @Inject
    private MyJMSClient jmsClient;

    @Inject
    private SimpleTestDestination simpleTestDestination;

    @Test
    public void simpleTest_1() throws JMSException {
	final MyJMSFunction<SimpleTestEntity, SimpleTestResult> function = jmsClient.createFunction(
		simpleTestDestination.getDestination(),
		SimpleTestResult.class);
	{
	    final String MESSAGE = "Hello JMS world!";
	    final SimpleTestEntity e = new SimpleTestEntity(MESSAGE);
	    final SimpleTestResult r = function.apply(e);
	    assertThat(r, not(nullValue()));
	    assertThat(r.message, allOf(not(nullValue()), is(equalTo(SimpleTestResult.PREFIX + e.message))));
	}
    }

    @Inject
    private ValidationTestDestination validationTestDestination;

    @Test
    public void validationTest_1() throws JMSException {
	final MyJMSFunction<ValidationTestEntity, ValidationTestResult> function = jmsClient.createFunction(
		validationTestDestination.getDestination(),
		ValidationTestResult.class);

	{
	    final String VALID_MESSAGE = "Hello JMS world!";
	    final ValidationTestEntity e = new ValidationTestEntity(VALID_MESSAGE);
	    final ValidationTestResult r = function.apply(e);
	    assertThat(r, not(nullValue()));
	    assertThat(r.getMessage(), allOf(not(nullValue()), is(equalTo(SimpleTestResult.PREFIX + e.getMessage()))));
	}
    }

    @Test
    public void validationTest_2() {
	final MyJMSFunction<ValidationTestEntity, ValidationTestResult> function = jmsClient.createFunction(
		validationTestDestination.getDestination(),
		ValidationTestResult.class);

	{
	    final String NULL_MESSAGE = null;
	    final ValidationTestEntity e = new ValidationTestEntity(NULL_MESSAGE);
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
    private RuntimeExceptionTestDestination runtimeExceptionTestDestination;

    @Test
    public void runtimeExceptionTest_1() throws JMSException {
	final MyJMSFunction<RuntimeExceptionTestEntity, RuntimeExceptionTestResult> function = jmsClient.createFunction(
		runtimeExceptionTestDestination.getDestination(),
		RuntimeExceptionTestResult.class);

	{
	    final String MESSAGE = "Hello JMS world!";
	    final RuntimeExceptionTestEntity e = new RuntimeExceptionTestEntity(MESSAGE);
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
