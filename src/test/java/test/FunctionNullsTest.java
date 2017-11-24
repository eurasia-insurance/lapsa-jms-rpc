package test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.jms.JMSException;

import org.junit.Test;

import ejb.resources.function.nulls.FunctionNullsDestination;
import ejb.resources.function.nulls.FunctionNullsEntity;
import ejb.resources.function.nulls.FunctionNullsResult;
import tech.lapsa.javax.jms.MyJMSClient;
import tech.lapsa.javax.jms.MyJMSClient.MyJMSFunction;

public class FunctionNullsTest extends ArquillianBaseTestCase {

    @Inject
    private MyJMSClient jmsClient;

    @Inject
    private FunctionNullsDestination destination;

    @Test
    public void basic() throws JMSException {
	final MyJMSFunction<FunctionNullsEntity, FunctionNullsResult> service = jmsClient
		.createFunction(destination.getDestination(), FunctionNullsResult.class);
	{
	    final FunctionNullsResult r = service.call(null);
	    assertThat(r, nullValue());
	}
    }
}
