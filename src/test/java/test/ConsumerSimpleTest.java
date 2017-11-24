package test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.jms.JMSException;

import org.junit.Test;

import ejb.resources.consumer.simple.ConsumerSimpleDestination;
import ejb.resources.consumer.simple.ConsumerSimpleEntity;
import tech.lapsa.javax.jms.MyJMSClient;
import tech.lapsa.javax.jms.MyJMSClient.MyJMSConsumer;

public class ConsumerSimpleTest extends ArquillianBaseTestCase {

    @Inject
    private MyJMSClient jmsClient;

    @Inject
    private ConsumerSimpleDestination consumerSimpleDestination;

    public static ConsumerSimpleEntity BASIC_EXPECTED = null;

    @Test
    public void basic() throws JMSException {
	final MyJMSConsumer<ConsumerSimpleEntity> function = jmsClient
		.createConsumer(consumerSimpleDestination.getDestination());
	{
	    final String MESSAGE = "Hello JMS world!";

	    final ConsumerSimpleEntity e = new ConsumerSimpleEntity(MESSAGE);
	    function.accept(e);
	    assertThat(BASIC_EXPECTED, allOf(not(nullValue()), is(equalTo(e))));
	}
    }
}
