package tech.lapsa.javax.jms;

import java.io.Serializable;

public @interface JmsServiceEntityType {
    Class<? extends Serializable> value();
}
