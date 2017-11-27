package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;

public interface JmsCallableService<E extends Serializable, R extends Serializable> {

    R calling(E entity, Properties properties);
}
