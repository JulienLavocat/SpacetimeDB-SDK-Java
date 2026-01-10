package eu.jlavocat.spacetimedb.messages.client;

import java.nio.ByteBuffer;

public final class ClientMessageEncoder {
    private ClientMessageEncoder() {
    }

    public static ByteBuffer encode(ClientMessage message) {
        return switch (message) {
            case ClientMessage.CallReducerMessage m -> m.toBsatn();
        };
    }
}
