package eu.jlavocat.spacetimedb;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

import eu.jlavocat.spacetimedb.events.OnConnectedEvent;
import eu.jlavocat.spacetimedb.events.OnDisconnectedEvent;

public class DbConnectionBuilder {

    private String uri = "http://localhost:3000";
    private String moduleName = "";
    private String token = "";

    private Optional<Consumer<OnConnectedEvent>> onConnect = Optional.empty();
    private Optional<Consumer<OnDisconnectedEvent>> onDisconnect = Optional.empty();

    public DbConnectionBuilder withUri(String uri) {
        this.uri = uri;
        return this;
    }

    public DbConnectionBuilder withModuleName(String moduleName) {
        this.moduleName = moduleName;
        return this;
    }

    public DbConnectionBuilder withToken(String token) {
        this.token = token;
        return this;
    }

    public DbConnectionBuilder onConnect(Consumer<OnConnectedEvent> onConnect) {
        this.onConnect = Optional.of(onConnect);
        return this;
    }

    public DbConnectionBuilder onDisconnect(Consumer<OnDisconnectedEvent> onDisconnect) {
        this.onDisconnect = Optional.of(onDisconnect);
        return this;
    }

    public void build() throws IOException, InterruptedException {

        byte[] connectionIdBytes = new byte[16];
        new Random().nextBytes(connectionIdBytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : connectionIdBytes) {
            sb.append(String.format("%02x", b));
        }
        String connectionId = sb.toString();

        if (moduleName.isEmpty()) {
            throw new IllegalArgumentException("Module name must be provided");
        }

        Websocket ws = new Websocket(uri, moduleName, token, onConnect, onDisconnect);
    }

}
