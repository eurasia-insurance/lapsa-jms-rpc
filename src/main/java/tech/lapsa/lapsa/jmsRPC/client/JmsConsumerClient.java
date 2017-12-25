package tech.lapsa.lapsa.jmsRPC.client;

import java.io.Serializable;
import java.util.Properties;

public interface JmsConsumerClient<E extends Serializable> extends JmsClient {

    void accept(E entity);

    void accept(E entity, Properties properties);
}