package tech.lapsa.lapsa.jmsRPC.client;

import java.io.Serializable;
import java.util.Properties;

public interface JmsCallableClient<E extends Serializable, R extends Serializable> extends JmsClient {

    R call(E entity);

    R call(E entity, Properties properties);
}