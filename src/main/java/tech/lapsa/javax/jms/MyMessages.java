package tech.lapsa.javax.jms;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageFormatException;

import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyStrings;

public final class MyMessages {

    private MyMessages() {
    }

    static final String PROPERTY_PREFIX = "MyMessages_";

    static Properties propertiesFromMessage(final Message message) throws JMSException {
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
	    } catch (MessageFormatException ignored) {
	    }
	}
	return p;
    }

    static void propertiesToJMSProducer(final JMSProducer producer, final Properties properties) {
	final Map<String, String> map = propertiesToMap(properties);
	for (Map.Entry<String, String> e : map.entrySet())
	    producer.setProperty(e.getKey(), e.getValue());
    }
    
    static void propertiesToMessage(final Message message, final Properties properties) throws JMSException {
	final Map<String, String> map = propertiesToMap(properties);
	for (Map.Entry<String, String> e : map.entrySet())
	    message.setStringProperty(e.getKey(), e.getValue());
    }

    private static Map<String, String> propertiesToMap(final Properties properties) {
	return properties.keySet().stream() //
		.filter(x -> MyObjects.isA(x, String.class))
		.map(Object::toString) //
		.filter(MyStrings::nonEmpty) //
		.collect(Collectors.toMap(x -> PROPERTY_PREFIX + x, x -> properties.getProperty(x)));
    }
}
