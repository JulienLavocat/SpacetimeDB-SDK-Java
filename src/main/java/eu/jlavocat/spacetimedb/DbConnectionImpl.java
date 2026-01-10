package eu.jlavocat.spacetimedb;

import java.util.concurrent.atomic.AtomicInteger;

import eu.jlavocat.spacetimedb.messages.client.CallReducer;
import eu.jlavocat.spacetimedb.messages.client.ClientMessage;
import eu.jlavocat.spacetimedb.messages.client.ClientMessageEncoder;

public class DbConnectionImpl {

    private Websocket ws;
    private boolean lightMode;

    private AtomicInteger requestIdCounter = new AtomicInteger();

    public DbConnectionImpl(Websocket ws, boolean lightMode) {
        this.ws = ws;
        this.lightMode = lightMode;
    }

    public void callReducer(String reducerName, byte[] args) {
        int requestId = requestIdCounter.incrementAndGet();
        CallReducer callReducerPayload = new CallReducer(reducerName, args, requestId, !lightMode);

        // TODO: Enqueue the message and send them in another part of the code to reuse
        // a single buffer for multiple messages, reducing allocations.

        ws.send(ClientMessageEncoder.encode(new ClientMessage.CallReducerMessage(callReducerPayload)));
    }
}
