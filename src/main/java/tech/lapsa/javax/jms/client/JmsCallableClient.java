package tech.lapsa.javax.jms.client;

import java.io.Serializable;
import java.util.Properties;

public interface JmsCallableClient<E extends Serializable, R extends Serializable> {

    R call(E entity);

    R call(E entity, Properties properties);
}