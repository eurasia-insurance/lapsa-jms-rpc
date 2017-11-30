package tech.lapsa.javax.jms;

import static tech.lapsa.javax.jms.commons.MyJMSs.*;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.jms.JMSRuntimeException;
import javax.jms.Message;
import javax.jms.MessageFormatException;

import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyStrings;

public final class Messages {

    private Messages() {
    }

    private static final String PROPERTY_PREFIX = "MyMessages_";

    public static Properties propertiesFromMessage(final Message message) throws JMSRuntimeException {
	return reThrowAsUnchecked(() -> {
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
	});
    }

    public static void propertiesToMessage(final Message message, final Properties properties)
	    throws JMSRuntimeException {
	reThrowAsUnchecked(() -> {
	    final Map<String, String> map = propertiesToMap(properties);
	    if (MyObjects.isNull(map))
		return;
	    for (final Map.Entry<String, String> entry : map.entrySet())
		message.setStringProperty(entry.getKey(), entry.getValue());
	});
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

}
