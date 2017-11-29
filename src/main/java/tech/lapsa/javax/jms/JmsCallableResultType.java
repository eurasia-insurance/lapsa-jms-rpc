package tech.lapsa.javax.jms;

import java.io.Serializable;

public @interface JmsServiceReturnType {
    Class<? extends Serializable> value();
}
