package eu.jlavocat.spacetimedb.messages.server;

import java.util.Optional;

public record SubscriptionError(long totalHostExecutionDurationMicros, Optional<Long> requestId, Optional<Long> queryId,
        Optional<TableId> tableId, String error) {
}
