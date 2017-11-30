package test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Properties;

import javax.inject.Inject;
import javax.jms.JMSException;

import org.junit.Test;

import ejb.resources.consumer.simple.ConsumerSimpleDestination;
import ejb.resources.consumer.simple.ConsumerSimpleDrivenBean;
import ejb.resources.consumer.simple.ConsumerSimpleEntity;
import tech.lapsa.javax.jms.client.JmsConsumerClient;
import tech.lapsa.javax.jms.client.JmsDestination;
import tech.lapsa.javax.jms.client.JmsEntityType;

public class ConsumerSimpleTest extends ArquillianBaseTestCase {

    @Inject
    @JmsDestination(ConsumerSimpleDestination.JNDI_NAME)
    @JmsEntityType(ConsumerSimpleEntity.class)
    private JmsConsumerClient<ConsumerSimpleEntity> consumerClient;

    public static ConsumerSimpleEntity BASIC_EXPECTED = null;

    @Test
    public void basic() throws JMSException {
	{
	    final String MESSAGE = "Hello JMS world!";

	    final ConsumerSimpleEntity e = new ConsumerSimpleEntity(MESSAGE);
	    consumerClient.accept(e);
	    assertThat(BASIC_EXPECTED, allOf(not(nullValue()), is(equalTo(e))));
	}
    }

    public static String WITH_PROPERTIES_EXPECTED = null;

    @Test
    public void withProperties() throws JMSException {
	{
	    final String MESSAGE = "Hello, %1$s!";
	    final String NAME = "John Bull";

	    final Properties properties = new Properties();
	    properties.setProperty(ConsumerSimpleDrivenBean.PROPERTY_NAME, NAME);

	    final ConsumerSimpleEntity e = new ConsumerSimpleEntity(MESSAGE);
	    consumerClient.accept(e, properties);
	    assertThat(WITH_PROPERTIES_EXPECTED, allOf(not(nullValue()), is(equalTo(NAME))));

	}
    }
}
