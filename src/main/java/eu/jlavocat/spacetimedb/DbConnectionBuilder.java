package eu.jlavocat.spacetimedb;

import java.io.IOException;
import java.util.Random;

public class DbConnectionBuilder {

    private String uri = "http://localhost:3000";
    private String moduleName = "";
    private String token = "";

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

        Websocket ws = new Websocket(uri, moduleName, token);

    }

}
