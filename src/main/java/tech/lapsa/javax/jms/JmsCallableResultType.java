package tech.lapsa.javax.jms;

import java.io.Serializable;

public @interface JmsCallableResultType {
    Class<? extends Serializable> value();
}
