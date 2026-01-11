package eu.jlavocat.spacetimedb.messages.server;

public record SubscribeMultiApplied(long requestId, long totalHostExecutionDurationMicros, long queryId,
        DatabaseUpdate update) {

}
