package test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.jms.JMSException;

import org.junit.Test;

import ejb.resources.callable.nulls.CallableNullsDestination;
import ejb.resources.callable.nulls.CallableNullsEntity;
import ejb.resources.callable.nulls.CallableNullsResult;
import tech.lapsa.javax.jms.JmsCallableResultType;
import tech.lapsa.javax.jms.JmsClientFactory.JmsCallable;
import tech.lapsa.javax.jms.JmsDestinationMappedName;
import tech.lapsa.javax.jms.JmsServiceEntityType;

public class CallableNullsTest extends ArquillianBaseTestCase {

    @Inject
    @JmsDestinationMappedName(CallableNullsDestination.GENERAL)
    @JmsServiceEntityType(CallableNullsEntity.class)
    @JmsCallableResultType(CallableNullsResult.class)
    private JmsCallable<CallableNullsEntity, CallableNullsResult> callable;

    @Test
    public void basic() throws JMSException {
	{
	    final CallableNullsResult r = callable.call(null);
	    assertThat(r, nullValue());
	}
    }
}
