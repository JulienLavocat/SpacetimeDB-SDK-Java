package eu.jlavocat.spacetimedb.messages.server;

import eu.jlavocat.spacetimedb.bsatn.ConnectionId;
import eu.jlavocat.spacetimedb.bsatn.Identity;

public record IdentityToken(Identity identity, String token, ConnectionId connectionId) {
}
