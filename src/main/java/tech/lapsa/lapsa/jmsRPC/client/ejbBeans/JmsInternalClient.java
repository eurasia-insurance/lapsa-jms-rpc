package tech.lapsa.javax.jms.client.ejbBeans;

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

import javax.ejb.Local;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Topic;

import tech.lapsa.javax.jms.client.ResponseNotReceivedException;

@Local
public interface JmsInternalClient {

    Message receiveReplyOn(UUID callId, Message message, long timeout)
	    throws JMSException, ResponseNotReceivedException;

    UUID sendWithReplyTo(Destination dest, Message message) throws JMSException;

    void send(Destination destination, Message... messages) throws JMSException;

    Message createMessage(Serializable entity);

    Message createMessage(Serializable entity, Properties properties);

    Queue createQueue(String queuePhysicalName);

    Topic createTopic(String topicPhysicalName);
}
