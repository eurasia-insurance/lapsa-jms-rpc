package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;

public abstract class ObjectConsumerDrivenBean<I extends Serializable>
	extends BaseDrivenBean<I, VoidResult> {

    protected ObjectConsumerDrivenBean(final Class<I> inC) {
	super(inC);
    }

    protected abstract void accept(I in, Properties properties);

    @Override
    final VoidResult _apply(final I in, final Properties properties) {
	accept(in, properties);
	return new VoidResult();
    }
}
