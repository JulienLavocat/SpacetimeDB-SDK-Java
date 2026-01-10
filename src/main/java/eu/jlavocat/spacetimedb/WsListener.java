package eu.jlavocat.spacetimedb;

import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

import eu.jlavocat.spacetimedb.bsatn.BsatnReader;
import eu.jlavocat.spacetimedb.events.OnConnectedEvent;
import eu.jlavocat.spacetimedb.events.OnDisconnectedEvent;
import eu.jlavocat.spacetimedb.messages.server.IdentityToken;
import eu.jlavocat.spacetimedb.messages.server.ServerMessage;
import eu.jlavocat.spacetimedb.messages.server.ServerMessageDecoder;

public final class WsListener implements WebSocket.Listener {
    private final Optional<Consumer<OnConnectedEvent>> onConnect;
    private final Optional<Consumer<OnDisconnectedEvent>> onDisconnect;

    public WsListener(Optional<Consumer<OnConnectedEvent>> onConnect,
            Optional<Consumer<OnDisconnectedEvent>> onDisconnect) {
        this.onConnect = onConnect;
        this.onDisconnect = onDisconnect;
    }

    @Override
    public void onOpen(WebSocket webSocket) {
        webSocket.request(1);
    }

    @Override
    public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last) {
        BsatnReader reader = new BsatnReader(data);
        System.out.println("Received binary message with " + reader.remaining() + " bytes, last=" + last);

        if (!last) {
            throw new IllegalStateException("Fragmented messages are not supported");
        }

        // First byte is compression algo
        byte compressionAlgo = reader.readByte();
        if (compressionAlgo != 0) {
            throw new IllegalStateException(
                    "Unsupported compression algorithm: " + compressionAlgo + ", only 0 (none) is supported");
        }

        var message = ServerMessageDecoder.decode(reader);

        switch (message) {
            case ServerMessage.IdentityTokenMessage(IdentityToken msg) -> {
                OnConnectedEvent event = new OnConnectedEvent(msg.identity(), msg.token(), msg.connectionId());
                onConnect.ifPresent(consumer -> consumer.accept(event));
            }
            default -> throw new IllegalStateException("Unexpected message type: " + message.getClass().getName());
        }

        webSocket.request(1);
        return CompletableFuture.completedStage(null);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        webSocket.request(1);
        return CompletableFuture.completedStage(null);
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        OnDisconnectedEvent event = new OnDisconnectedEvent(statusCode, reason);
        onDisconnect.ifPresent(consumer -> consumer.accept(event));
        return CompletableFuture.completedStage(null);
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        System.err.println("WebSocket error: " + error.getMessage());
        error.printStackTrace();
    }

}
