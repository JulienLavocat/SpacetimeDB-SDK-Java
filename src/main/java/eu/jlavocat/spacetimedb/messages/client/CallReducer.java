package eu.jlavocat.spacetimedb.messages.client;

public record CallReducer(String reducer, byte[] args, int requestId, boolean isLightMode) {
}
