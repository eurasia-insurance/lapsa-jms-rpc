package tech.lapsa.javax.jms;

import java.io.Serializable;

final class VoidResult implements Serializable {
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
	return obj != null && obj instanceof VoidResult;
    }

    @Override
    public int hashCode() {
	return 1;
    }
}