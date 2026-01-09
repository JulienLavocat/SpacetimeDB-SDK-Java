package eu.jlavocat.spacetimedb.events;

import eu.jlavocat.spacetimedb.bsatn.ConnectionId;
import eu.jlavocat.spacetimedb.bsatn.Identity;

public record OnConnectedEvent(Identity identity, String token, ConnectionId connectionId) {

}
