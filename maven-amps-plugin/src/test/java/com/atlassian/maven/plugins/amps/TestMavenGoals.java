package com.atlassian.maven.plugins.amps;

import junit.framework.TestCase;

import java.io.IOException;
import java.net.ServerSocket;

import org.apache.maven.plugin.PluginManager;

/**
 *
 */
public class TestMavenGoals extends TestCase
{
    public void testPickFreePort() throws IOException
    {
        MavenContext ctx = new MavenContext(null, null, null, (PluginManager) null, null);
        MavenGoals goals = new MavenGoals(ctx);
        ServerSocket socket = null;
        try
        {
            socket = new ServerSocket(16829);

            // Pick any
            int port = goals.pickFreePort(0);
            assertTrue(16829 != port);
            assertTrue(port > 0);

            // Pick taken
            port = goals.pickFreePort(16829);
            assertTrue(16829 != port);
            assertTrue(port > 0);

            // Pick free
            assertEquals(16828, goals.pickFreePort(16828));
        }
        finally
        {
            socket.close();
        }
    }
}
