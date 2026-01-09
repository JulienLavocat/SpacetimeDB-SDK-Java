package eu.jlavocat.spacetimedb.messages.server;

public sealed interface ServerMessage permits ServerMessage.IdentityTokenMessage {

    record IdentityTokenMessage(IdentityToken payload) implements ServerMessage {
    }

}
