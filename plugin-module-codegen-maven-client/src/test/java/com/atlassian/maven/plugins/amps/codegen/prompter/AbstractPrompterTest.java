package com.atlassian.maven.plugins.amps.codegen.prompter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.atlassian.plugins.codegen.modules.PluginModuleLocation;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.junit.After;
import org.junit.Before;

import static org.mockito.Mockito.mock;

/**
 * @since 3.6
 */
public abstract class AbstractPrompterTest
{
    protected File tempDir;
    protected File srcDir;
    protected File testDir;
    protected File resourcesDir;
    protected File templateDir;
    protected File pluginXml;
    protected PluginModuleLocation moduleLocation;
    protected Prompter prompter;

    @Before
    public void setupDirs() throws Exception
    {

        final File sysTempDir = new File("target");
        String dirName = UUID.randomUUID()
                .toString();
        tempDir = new File(sysTempDir, dirName);
        srcDir = new File(tempDir, "src");
        testDir = new File(tempDir, "test-src");
        resourcesDir = new File(tempDir, "resources");
        templateDir = new File(resourcesDir, "templates");
        pluginXml = new File(resourcesDir, "atlassian-plugin.xml");

        tempDir.mkdirs();
        srcDir.mkdirs();
        resourcesDir.mkdirs();
        templateDir.mkdirs();

        InputStream is = this.getClass()
                .getResourceAsStream("/empty-plugin.xml");
        IOUtils.copy(is, FileUtils.openOutputStream(pluginXml));

        moduleLocation = new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build();
        prompter = mock(Prompter.class);
    }

    @After
    public void removeTempDir() throws IOException
    {
        FileUtils.deleteDirectory(tempDir);
    }
}
