package test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.validation.ValidationException;

import org.junit.Test;

import tech.lapsa.javax.jms.InvalidResponseTypeException;
import tech.lapsa.javax.jms.MyJMSClient;
import tech.lapsa.javax.jms.MyJMSClient.MyJMSFunction;
import test.ejb.resources.simpletest.SimpleTestDestination;
import test.ejb.resources.simpletest.SimpleTestEntity;
import test.ejb.resources.simpletest.SimpleTestResult;
import tech.lapsa.javax.jms.ResponseNotReceivedException;

public class ObjectFunctionTest extends ArquillianBaseTestCase {

    @Inject
    private MyJMSClient jmsClient;

    @Inject
    private SimpleTestDestination simpleTestDestination;

    @Test
    public void simpleTest() throws ValidationException, ResponseNotReceivedException, InvalidResponseTypeException,
	    JMSException, RuntimeException {
	final MyJMSFunction<SimpleTestEntity, SimpleTestResult> function = jmsClient.createFunction(
		simpleTestDestination.getDestination(),
		SimpleTestResult.class);
	final SimpleTestEntity e = new SimpleTestEntity("Hello JMS world!");
	final SimpleTestResult r = function.apply(e);
	assertThat(r, not(nullValue()));
	assertThat(r.message, allOf(not(nullValue()), is(equalTo(SimpleTestResult.PREFIX + e.message))));
    }
}
