package tech.lapsa.javax.jms.internal;

import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.JMSRuntimeException;
import javax.jms.Message;
import javax.jms.MessageFormatException;
import javax.jms.Queue;
import javax.jms.Topic;

import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;

public final class MyMessages {

    private MyMessages() {
    }

    private static final String PROPERTY_PREFIX = "MyMessages_";

    public static Properties propertiesFromMessage(final Message message) throws JMSException {
	final Properties p = new Properties();
	final Enumeration<?> en = message.getPropertyNames();
	while (en.hasMoreElements()) {
	    final Object e = en.nextElement();
	    if (MyObjects.isNotA(e, String.class))
		continue;
	    final String k = (String) e;
	    if (!k.startsWith(PROPERTY_PREFIX))
		continue;
	    try {
		final String v = message.getStringProperty(k);
		final String mk = k.substring(PROPERTY_PREFIX.length());
		p.setProperty(mk, v);
	    } catch (final MessageFormatException ignored) {
	    }
	}
	if (p.isEmpty())
	    return null;
	return p;
    }

    static void propertiesToJMSProducer(final JMSProducer producer, final Properties properties) {
	final Map<String, String> map = propertiesToMap(properties);
	if (MyObjects.isNull(map))
	    return;
	for (final Map.Entry<String, String> e : map.entrySet())
	    producer.setProperty(e.getKey(), e.getValue());
    }

    public static void propertiesToMessage(final Message message, final Properties properties)
	    throws JMSRuntimeException {
	final Map<String, String> map = propertiesToMap(properties);
	if (MyObjects.isNull(map))
	    return;
	for (final Map.Entry<String, String> entry : map.entrySet())
	    try {
		message.setStringProperty(entry.getKey(), entry.getValue());
	    } catch (JMSException e) {
		throw uchedked(e);
	    }
    }

    private static Map<String, String> propertiesToMap(final Properties properties) {
	if (MyObjects.isNull(properties))
	    return null;
	return properties.keySet().stream() //
		.filter(x -> MyObjects.isA(x, String.class))
		.map(Object::toString) //
		.filter(MyStrings::nonEmpty) //
		.collect(Collectors.toMap(x -> PROPERTY_PREFIX + x, x -> properties.getProperty(x)));
    }

    public static JMSRuntimeException uchedked(JMSException e) {
	return new JMSRuntimeException(e.getMessage(), e.getErrorCode(), e.getCause());
    }

    public static Optional<String> optJMSMessageIDOf(final Message m) {
	return MyOptionals.of(m) //
		.map(mmm -> {
		    try {
			return mmm.getJMSMessageID();
		    } catch (JMSException e) {
			return null;
		    }
		});
    }

    public static Optional<String> optJMSCorellationIDOf(final Message m) {
	return MyOptionals.of(m) //
		.map(mmm -> {
		    try {
			return mmm.getJMSCorrelationID();
		    } catch (JMSException e) {
			return null;
		    }
		});
    }

    public static Optional<String> optJMSDestination(final Message m) {
	return MyOptionals.of(m) //
		.map(mmm -> {
		    try {
			return mmm.getJMSDestination();
		    } catch (JMSException e) {
			return null;
		    }
		}) //
		.flatMap(MyMessages::optNameOf);
    }

    public static String getJMSDestination(final Message m) {
	return optJMSDestination(m).orElse("Unknwon JMSDestination");
    }

    public static String getJMSCorellationIDOf(final Message m) {
	return optJMSCorellationIDOf(m).orElse("Unknwon JMSCorellationID");
    }

    public static String getJMSMessageIDOf(final Message m) {
	return optJMSMessageIDOf(m).orElse("Unknwon JMSMessageID");
    }

    public static String getNameOf(final Destination d) {
	return optNameOf(d).orElse("Unknown Destination name");
    }

    public static Optional<String> optNameOf(final Destination d) {
	final Optional<String> tryQueue = MyObjects.optionalA(d, Queue.class) //
		.map(q -> {
		    try {
			return q.getQueueName();
		    } catch (JMSException e) {
			return null;
		    }
		}) //
		.map(x -> "Queue('" + x + "')");
	if (tryQueue.isPresent())
	    return tryQueue;
	final Optional<String> tryTopic = MyObjects.optionalA(d, Topic.class) //
		.map(q -> {
		    try {
			return q.getTopicName();
		    } catch (JMSException e) {
			return null;
		    }
		}) //
		.map(x -> "Topic('" + x + "')");
	if (tryTopic.isPresent())
	    return tryTopic;

	return Optional.empty();
    }
}
