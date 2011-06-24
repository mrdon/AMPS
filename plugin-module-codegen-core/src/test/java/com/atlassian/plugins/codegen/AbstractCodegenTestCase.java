package com.atlassian.plugins.codegen;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Author: jdoklovic
 */
public abstract class AbstractCodegenTestCase {
    protected File tempDir;
    protected File srcDir;
    protected File resourcesDir;
    protected File templateDir;
    protected File pluginXml;

    @Before
    public void setup() throws IOException {
        final File sysTempDir = new File("target");
        String dirName = UUID.randomUUID().toString();
        tempDir = new File(sysTempDir, dirName);
        srcDir = new File(tempDir, "src");
        resourcesDir = new File(tempDir, "resources");
        templateDir = new File(resourcesDir, "templates");
        pluginXml = new File(resourcesDir, "atlassian-plugin.xml");

        tempDir.mkdirs();
        srcDir.mkdirs();
        resourcesDir.mkdirs();
        templateDir.mkdirs();

        InputStream is = this.getClass().getResourceAsStream("/empty-plugin.xml");
        IOUtils.copy(is, FileUtils.openOutputStream(pluginXml));

    }

    @After
    public void removeTempDir() throws IOException {
        // FileUtils.deleteDirectory(tempDir);
    }
}
