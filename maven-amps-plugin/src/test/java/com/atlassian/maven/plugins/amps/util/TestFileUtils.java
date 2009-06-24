package com.atlassian.maven.plugins.amps.util;

import junit.framework.TestCase;

import java.io.File;

import static com.atlassian.maven.plugins.amps.util.FileUtils.file;

public class TestFileUtils extends TestCase
{
    public void testFile()
    {
        File parent = new File("bob");
        assertEquals(new File(parent, "jim").getAbsolutePath(), file(parent, "jim").getAbsolutePath());

        assertEquals(new File(new File(parent, "jim"), "sarah").getAbsolutePath(),
                file(parent, "jim", "sarah").getAbsolutePath());
    }
}
