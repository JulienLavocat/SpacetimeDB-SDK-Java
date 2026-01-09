package eu.jlavocat.spacetimedb.messages.client;

public sealed interface ClientMessage permits ClientMessage.CallReducerMessage {

    record CallReducerMessage(CallReducer payload) implements ClientMessage {
    }

}
