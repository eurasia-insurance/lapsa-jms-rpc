package tech.lapsa.javax.jms.client;

import java.io.Serializable;
import java.util.Properties;

public interface JmsConsumerClient<E extends Serializable> {

    void accept(E entity);

    void accept(E entity, Properties properties);
}