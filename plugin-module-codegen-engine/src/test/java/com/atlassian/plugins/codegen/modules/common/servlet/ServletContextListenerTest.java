package com.atlassian.plugins.codegen.modules.common.servlet;

import java.io.File;
import java.io.IOException;

import com.atlassian.plugins.codegen.AbstractCodegenTestCase;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
//TODO: update test to use Dom4J

/**
 * @since 3.6
 */
public class ServletContextListenerTest extends AbstractCodegenTestCase<ServletContextListenerProperties>
{
    public static final String PACKAGE_NAME = "com.atlassian.plugins.servlet.listener";

    @Before
    public void runGenerator() throws Exception
    {
        setCreator(new ServletContextListenerModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new ServletContextListenerProperties(PACKAGE_NAME + ".MyServletContextListener"));

        props.setIncludeExamples(false);

        creator.createModule(moduleLocation, props);
    }

    @Test
    public void allFilesAreGenerated() throws Exception
    {
        String packagePath = PACKAGE_NAME.replaceAll("\\.", File.separator);
        assertTrue("main class not generated", new File(srcDir, packagePath + File.separator + "MyServletContextListener.java").exists());
        assertTrue("test class not generated", new File(testDir, packagePath + File.separator + "MyServletContextListenerTest.java").exists());
        assertTrue("main class not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void pluginXmlContainsModule() throws IOException
    {
        String pluginXmlContent = FileUtils.readFileToString(pluginXml);

        assertTrue("module not found in plugin xml", pluginXmlContent.contains("<servlet-context-listener"));
        assertTrue("module class not found in plugin xml", pluginXmlContent.contains("class=\"" + PACKAGE_NAME + ".MyServletContextListener\""));
    }

}
