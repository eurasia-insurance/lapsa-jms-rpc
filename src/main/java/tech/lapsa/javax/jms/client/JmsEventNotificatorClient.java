package tech.lapsa.javax.jms.client;

import java.io.Serializable;
import java.util.Properties;

public interface JmsEventNotificatorClient<E extends Serializable> {

    void eventNotify(E entity, Properties properties);

    @SuppressWarnings("unchecked")
    void eventNotify(E... entities);
}