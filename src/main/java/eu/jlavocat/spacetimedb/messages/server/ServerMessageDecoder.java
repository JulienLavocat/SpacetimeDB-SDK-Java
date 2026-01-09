package eu.jlavocat.spacetimedb.messages.server;

import eu.jlavocat.spacetimedb.bsatn.BsatnReader;
import eu.jlavocat.spacetimedb.bsatn.ConnectionId;
import eu.jlavocat.spacetimedb.bsatn.Identity;

public final class ServerMessageDecoder {

    private static final byte IDENTITY_TOKEN_MESSAGE = 3;

    private ServerMessageDecoder() {
    }

    public static ServerMessage decode(BsatnReader reader) {
        byte serverMsgType = reader.readByte();
        System.out.println("Server message type: " + serverMsgType);

        return switch (serverMsgType) {
            case IDENTITY_TOKEN_MESSAGE -> decodeIdentityTokenMessage(reader);
            default -> throw new IllegalStateException("Unknown server message type: " + serverMsgType);
        };
    }

    private static ServerMessage.IdentityTokenMessage decodeIdentityTokenMessage(BsatnReader reader) {
        var identity = new Identity(reader.readU256());
        var token = reader.readString();
        var connectionId = new ConnectionId(reader.readU128());

        IdentityToken payload = new IdentityToken(identity, token, connectionId);
        return new ServerMessage.IdentityTokenMessage(payload);
    }
}
