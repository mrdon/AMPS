package com.atlassian.maven.plugins.amps;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.jar.Manifest;

import com.google.common.collect.ImmutableMap;

import org.apache.maven.model.Build;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;

import static com.atlassian.maven.plugins.amps.util.FileUtils.file;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestMavenGoals
{
    private MavenContext ctx;
    private MavenProject project;
    private Build build;
    private MavenGoals goals;
    
    @Before
    public void setUp()
    {
        project = mock(MavenProject.class);
        build = mock(Build.class);
        ctx = mock(MavenContext.class);
        when(project.getBuild()).thenReturn(build);
        when(ctx.getProject()).thenReturn(project);
        
        goals = new MavenGoals(ctx);
    }
    
    @Test
    public void testPickFreePort() throws IOException
    {
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
    
    @Test
    public void testGenerateMinimalManifest() throws Exception
    {
        File tempDir = File.createTempFile("TestMavenGoals", "dir");
        tempDir.delete();
        tempDir.mkdir();
        
        when(build.getOutputDirectory()).thenReturn(tempDir.getAbsolutePath());

        Map<String, String> attrs = ImmutableMap.of("Attribute-A", "aaa", "Attribute-B", "bbb");
        
        goals.generateMinimalManifest(attrs);
        
        File mf = file(tempDir.getAbsolutePath(), "META-INF", "MANIFEST.MF");
        assertTrue(mf.exists());
        
        Manifest m = new Manifest(new FileInputStream(mf));
        assertEquals("aaa", m.getMainAttributes().getValue("Attribute-A"));
        assertEquals("bbb", m.getMainAttributes().getValue("Attribute-B"));
        assertNull(m.getMainAttributes().getValue("Bundle-SymbolicName"));
    }
}
