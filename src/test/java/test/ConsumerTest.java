package test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.jms.JMSException;

import org.junit.Test;

import tech.lapsa.javax.jms.MyJMSClient;
import tech.lapsa.javax.jms.MyJMSClient.MyJMSConsumer;
import test.ejb.resources.consumer.simple.ConsumerSimpleDestination;
import test.ejb.resources.consumer.simple.ConsumerSimpleDrivenBean;
import test.ejb.resources.consumer.simple.SimpleEntity;

public class ConsumerTest extends ArquillianBaseTestCase {

    @Inject
    private MyJMSClient jmsClient;

    @Inject
    private ConsumerSimpleDestination consumerSimpleDestination;

    @Test
    public void simpleTest_1() throws JMSException {
	final MyJMSConsumer<SimpleEntity> function = jmsClient.createConsumer(consumerSimpleDestination.getDestination());
	{
	    final String MESSAGE = "Hello JMS world!";
	    final SimpleEntity e = new SimpleEntity(MESSAGE);
	    function.accept(e);
	    assertThat(ConsumerSimpleDrivenBean.lastReceived, allOf(not(nullValue()), is(equalTo(e))));
	}
    }
}
