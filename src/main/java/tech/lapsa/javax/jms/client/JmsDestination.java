package tech.lapsa.javax.jms.client;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ FIELD, TYPE, METHOD })
public @interface JmsDestination {
    String value();
}
