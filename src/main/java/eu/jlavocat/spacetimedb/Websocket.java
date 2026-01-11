package eu.jlavocat.spacetimedb;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Builder;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.function.Consumer;

import eu.jlavocat.spacetimedb.events.OnConnectedEvent;
import eu.jlavocat.spacetimedb.events.OnDisconnectedEvent;
import eu.jlavocat.spacetimedb.messages.server.IdentityToken;

public class Websocket {

    private WebSocket webSocket;

    public Websocket(String uri, String moduleName, String token, Consumer<IdentityToken> onIdentityToken,
            Optional<Consumer<OnConnectedEvent>> onConnect,
            Optional<Consumer<OnDisconnectedEvent>> onDisconnect) {
        String fullUri = String.format("%s/v1/database/%s/subscribe?compression=None", uri, moduleName).replace("http",
                "ws");
        URI wsUri = URI.create(fullUri);
        Builder webSocketBuilder = HttpClient.newHttpClient()
                .newWebSocketBuilder()
                .subprotocols("v1.bsatn.spacetimedb");

        if (!token.isEmpty()) {
            webSocketBuilder.header("Authorization", "Bearer " + token);
        }

        this.webSocket = webSocketBuilder
                .buildAsync(wsUri, new WsListener(onConnect, onDisconnect, onIdentityToken))
                .join();
    }

    public void send(ByteBuffer data) {
        webSocket.sendBinary(data, true);
    }

}
