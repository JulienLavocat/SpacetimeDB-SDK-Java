package eu.jlavocat.spacetimedb;

import java.io.IOException;

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

    public void testApp() throws IOException, InterruptedException {
        DbConnectionBuilder builder = new DbConnectionBuilder().withModuleName("nova9");
        builder.build();
    }
}
