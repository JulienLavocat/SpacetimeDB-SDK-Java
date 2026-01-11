package eu.jlavocat.spacetimedb;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import eu.jlavocat.spacetimedb.bsatn.ConnectionId;
import eu.jlavocat.spacetimedb.bsatn.Identity;
import eu.jlavocat.spacetimedb.events.OnConnectedEvent;
import eu.jlavocat.spacetimedb.events.OnDisconnectedEvent;
import eu.jlavocat.spacetimedb.messages.client.ClientMessage;
import eu.jlavocat.spacetimedb.messages.server.IdentityToken;

public class DbConnectionImpl {

    private Websocket ws;
    private boolean lightMode;

    private AtomicLong requestIdCounter = new AtomicLong();
    private AtomicLong queryIdCounter = new AtomicLong();

    private Identity identity;
    private String token;
    private ConnectionId connectionId;

    public DbConnectionImpl(String uri, String moduleName, String token,
            Optional<Consumer<OnConnectedEvent>> onConnect,
            Optional<Consumer<OnDisconnectedEvent>> onDisconnect, boolean lightMode) {
        this.ws = new Websocket(uri, moduleName, token,
                (IdentityToken msg) -> {
                    this.identity = msg.identity();
                    this.token = msg.token();
                    this.connectionId = msg.connectionId();
                },
                onConnect,
                onDisconnect);
        this.lightMode = lightMode;
        this.identity = null;
    }

    public Identity identity() {
        return identity;
    }

    public String token() {
        return token;
    }

    public ConnectionId connectionId() {
        return connectionId;
    }

    public void callReducer(String reducerName, byte[] args) {
        long requestId = requestIdCounter.incrementAndGet();

        // TODO: Enqueue the message and send them in another part of the code to reuse
        // a single buffer for multiple messages, reducing allocations.

        ws.send(new ClientMessage.CallReducerMessage(reducerName, args, requestId, lightMode).toBsatn());
    }

    public void subscribe(String[] queries) {
        long requestId = requestIdCounter.incrementAndGet();
        long queryId = queryIdCounter.incrementAndGet();

        // TODO: Enqueue the message and send them in another part of the code to reuse
        // a single buffer for multiple messages, reducing allocations.

        ws.send(new ClientMessage.SubscribeMultiMessage(queries, requestId, queryId).toBsatn());
    }

}
