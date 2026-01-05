package eu.jlavocat.spacetimedb;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Builder;
import java.net.http.WebSocket.Listener;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;

public class Websocket {

    private WebSocket webSocket;

    public Websocket(String uri, String moduleName, String token) {
        String fullUri = String.format("%s/v1/database/%s/subscribe", uri, moduleName).replace("http", "ws");
        URI wsUri = URI.create(fullUri);
        Builder webSocketBuilder = HttpClient.newHttpClient()
                .newWebSocketBuilder()
                .subprotocols("v1.bsatn.spacetimedb");

        if (!token.isEmpty()) {
            webSocketBuilder.header("Authorization", "Bearer " + token);
        }

        this.webSocket = webSocketBuilder
                .buildAsync(wsUri, new WebSocket.Listener() {
                    @Override
                    public void onOpen(WebSocket webSocket) {
                        System.out.println("WebSocket opened");
                        Listener.super.onOpen(webSocket);
                    }

                    @Override
                    public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last) {
                        System.out.println("Recv: " + new String(data.array()));
                        return Listener.super.onBinary(webSocket, data, last);
                    }
                })
                .join();
    }

}
