package com.atlassian.maven.plugins.amps.util;

import static com.atlassian.maven.plugins.amps.util.FileUtils.doesFileNameMatchArtifact;
import static com.atlassian.maven.plugins.amps.util.FileUtils.file;
import junit.framework.TestCase;

import java.io.File;

public class TestFileUtils extends TestCase
{
    public void testFile()
    {
        File parent = new File("bob");
        assertEquals(new File(parent, "jim").getAbsolutePath(), file(parent, "jim").getAbsolutePath());

        assertEquals(new File(new File(parent, "jim"), "sarah").getAbsolutePath(),
                file(parent, "jim", "sarah").getAbsolutePath());
    }

    public void testDoesFileNameMatcheArtifact()
    {
        assertTrue(doesFileNameMatchArtifact("sal-crowd-plugin-2.0.7.jar", "sal-crowd-plugin"));
        assertFalse(doesFileNameMatchArtifact("sal-crowd-plugin-2.0.7.jar", "crowd-plugin"));
    }
}
