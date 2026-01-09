package eu.jlavocat.spacetimedb.bsatn;

public record ConnectionId(U128 value) {
    @Override
    public String toString() {
        return value.toString();
    }
}
