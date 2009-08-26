package com.atlassian.amps.accept;

import static com.atlassian.amps.accept.AmpsHelper.runAmpsScript;

import java.io.File;
import java.io.IOException;

public class TestCreateAndVerifyPlugin extends AmpsTestBase
{
    public void testJIRA() throws IOException
    {
        createAndVerify("jira");
    }

    public void testRefapp() throws IOException
    {
        createAndVerify("refapp");
    }

    public void testConfluence() throws IOException
    {
        createAndVerify("confluence");
    }


    private void createAndVerify(String productId)
            throws IOException
    {
        runAmpsScript(ampsHome, baseDir, "amps-create-" + productId + "-plugin",
                "-a", "foo-" + productId + "-plugin",
                "-g", "com.example",
                "-p", "com.example.foo",
                "-v", "1.0-SNAPSHOT",
                "-i", "false");

        File appDir = new File(baseDir, "foo-" + productId + "-plugin");
        assertTrue(appDir.exists());

        /* Restore these once AMPS-82 is done
        runAmpsScript(ampsHome, appDir, "amps-mvn", "verify");

        File pluginJar = new File(new File(appDir, "target"), "foo-" + productId + "-plugin-1.0-SNAPSHOT.jar");
        assertTrue(pluginJar.exists());
        */
    }
}
