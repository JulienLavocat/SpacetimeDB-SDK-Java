package eu.jlavocat.spacetimedb.events;

public record OnDisconnectedEvent(int statusCode, String reason) {
}
