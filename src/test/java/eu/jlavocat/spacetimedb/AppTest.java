package eu.jlavocat.spacetimedb;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AppTest
        extends TestCase {

    public AppTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    // public void testConnect() throws IOException, InterruptedException {
    // CountDownLatch latch = new CountDownLatch(1);
    //
    // DbConnectionBuilder builder = new DbConnectionBuilder()
    // .withUri("http://localhost:4000")
    // .withModuleName("nova9")
    // .onConnect((v) -> {
    // System.out.println("Connected event received: " + v);
    // latch.countDown();
    // });
    // builder.build();
    //
    // boolean connected = latch.await(10, TimeUnit.SECONDS);
    // assertTrue("Should have received connected event", connected);
    // }

    // public void testCallReducer() throws IOException, InterruptedException {
    // CountDownLatch connectedLatch = new CountDownLatch(1);
    // CountDownLatch reducerResultLatch = new CountDownLatch(2);
    //
    // DbConnectionBuilder builder = new DbConnectionBuilder()
    // .withUri("http://localhost:4000")
    // .withModuleName("nova9")
    // .onConnect((v) -> {
    // connectedLatch.countDown();
    // System.out.println("Connected event received: " + v);
    // }).onDisconnect((v) -> {
    // System.out.println("Disconnected event received: " + v);
    // });
    //
    // DbConnectionImpl connection = builder.build();
    //
    // boolean connected = connectedLatch.await(10, TimeUnit.SECONDS);
    // assertTrue("Should have received connected event", connected);
    //
    // BsatnWriter w = new BsatnWriter();
    // w.writeIdentity(connection.identity());
    // w.writeU64(123456789L);
    // w.writeBool(false);
    // byte[] args = w.toByteArray();
    //
    // connection.callReducer("set_current_system", args);
    //
    // boolean hasResult = reducerResultLatch.await(5, TimeUnit.SECONDS);
    // assertTrue("Should have received reducer result", hasResult);
    // }

    public void testSubscription() throws InterruptedException, IOException {
        CountDownLatch connectedLatch = new CountDownLatch(1);
        CountDownLatch subscribeAppliedLatch = new CountDownLatch(2);

        DbConnectionBuilder builder = new DbConnectionBuilder()
                .withUri("http://localhost:4000")
                .withModuleName("nova9")
                .onConnect((v) -> {
                    connectedLatch.countDown();
                    System.out.println("Connected event received: " + v);
                }).onDisconnect((v) -> {
                    System.out.println("Disconnected event received: " + v);
                });

        DbConnectionImpl connection = builder.build();

        boolean connected = connectedLatch.await(10, TimeUnit.SECONDS);
        assertTrue("Should have received connected event", connected);

        connection.subscribe(new String[] { "SELECT * FROM *" });

        boolean hasResult = subscribeAppliedLatch.await(20, TimeUnit.SECONDS);
        assertTrue("Should have received reducer result", hasResult);
    }

}
