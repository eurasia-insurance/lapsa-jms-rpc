package test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.jms.JMSException;

import org.junit.Test;

import ejb.resources.consumer.simple.ConsumerSimpleDestination;
import ejb.resources.consumer.simple.ConsumerSimpleDrivenBean;
import ejb.resources.consumer.simple.SimpleEntity;
import tech.lapsa.javax.jms.MyJMSClient;
import tech.lapsa.javax.jms.MyJMSClient.MyJMSConsumer;

public class ConsumerSimpleTest extends ArquillianBaseTestCase {

    @Inject
    private MyJMSClient jmsClient;

    @Inject
    private ConsumerSimpleDestination consumerSimpleDestination;

    @Test
    public void simple() throws JMSException {
	final MyJMSConsumer<SimpleEntity> function = jmsClient.createConsumer(consumerSimpleDestination.getDestination());
	{
	    final String MESSAGE = "Hello JMS world!";
	    final SimpleEntity e = new SimpleEntity(MESSAGE);
	    function.accept(e);
	    assertThat(ConsumerSimpleDrivenBean.lastReceived, allOf(not(nullValue()), is(equalTo(e))));
	}
    }
}
