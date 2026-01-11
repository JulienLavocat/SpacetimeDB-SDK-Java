package eu.jlavocat.spacetimedb.messages.server;

import eu.jlavocat.spacetimedb.bsatn.BsatnReader;

public sealed interface UpdateStatus
        permits UpdateStatus.Committed, UpdateStatus.Failed, UpdateStatus.OutOfEnergy {

    static final byte UPDATE_STATUS_COMMITTED = 0;
    static final byte UPDATE_STATUS_FAILED = 1;
    static final byte UPDATE_STATUS_OUT_OF_ENERGY = 2;

    record Committed(DatabaseUpdate update) implements UpdateStatus {
    }

    record Failed(String reason) implements UpdateStatus {
    }

    record OutOfEnergy() implements UpdateStatus {
    }

    public static UpdateStatus fromBsatn(BsatnReader r) {
        byte statusType = r.readByte();
        return switch (statusType) {
            case UPDATE_STATUS_COMMITTED -> new Committed(DatabaseUpdate.fromBsatn(r));
            case UPDATE_STATUS_FAILED -> new Failed(r.readString());
            case UPDATE_STATUS_OUT_OF_ENERGY -> new OutOfEnergy();
            default -> throw new IllegalStateException("Unknown UpdateStatus type: " + statusType);
        };
    }

}
