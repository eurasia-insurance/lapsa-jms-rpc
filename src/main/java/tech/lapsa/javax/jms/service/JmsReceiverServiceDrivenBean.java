package tech.lapsa.javax.jms;

import java.io.Serializable;
import java.util.Properties;

import tech.lapsa.javax.jms.ConsumerServiceDrivenBean.VoidResult;
import tech.lapsa.javax.jms.internal.JmsInternalServiceBaseDrivenBean;

public abstract class ConsumerServiceDrivenBean<E extends Serializable>
	extends JmsInternalServiceBaseDrivenBean<E, VoidResult> implements JmsReceiverService<E> {

    protected ConsumerServiceDrivenBean(final Class<E> entityClazz) {
	super(entityClazz);
    }

    @Override
    protected final VoidResult _apply(final E entity, final Properties properties) {
	receiving(entity, properties);
	return VoidResult.INSTANCE;
    }

    static final class VoidResult implements Serializable {
	private static final long serialVersionUID = 1L;

	private VoidResult() {
	}

	public static VoidResult INSTANCE = new VoidResult();

	@Override
	public String toString() {
	    return "VOID";
	}

	@Override
	public boolean equals(Object obj) {
	    return obj == this || (obj != null && obj instanceof VoidResult);
	}

	@Override
	public int hashCode() {
	    return 1;
	}
    }
}
