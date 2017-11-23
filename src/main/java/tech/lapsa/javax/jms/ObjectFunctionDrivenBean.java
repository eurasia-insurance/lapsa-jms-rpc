package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;

public abstract class ObjectFunctionDrivenBean<I extends Serializable, O extends Serializable>
	extends BaseDrivenBean<I, O> {

    protected ObjectFunctionDrivenBean(final Class<I> inC) {
	super(inC);
    }

    protected abstract O apply(I in, Properties properties);

    @Override
    final O _apply(final I in, final Properties properties) {
	return apply(in, properties);
    }
}
