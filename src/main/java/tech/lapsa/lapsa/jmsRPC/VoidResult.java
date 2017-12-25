package tech.lapsa.lapsa.jmsRPC;

import java.io.Serializable;

public final class VoidResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private VoidResult() {
    }

    public static VoidResult INSTANCE = new VoidResult();

    @Override
    public String toString() {
	return "VOID";
    }

    @Override
    public boolean equals(final Object obj) {
	return obj == this || obj != null && obj instanceof VoidResult;
    }

    @Override
    public int hashCode() {
	return 1;
    }
}