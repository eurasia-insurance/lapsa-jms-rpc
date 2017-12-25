package tech.lapsa.lapsa.jmsRPC.service;

import java.io.Serializable;
import java.util.Properties;

public interface JmsReceiverService<E extends Serializable> {

    void receiving(E entity, Properties properties);
}
