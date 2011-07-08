package com.atlassian.plugins.codegen.modules.common.servlet;

import com.atlassian.plugins.codegen.AbstractCodegenTestCase;
import com.atlassian.plugins.codegen.modules.PluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleCreatorRegistry;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @since version
 */
public class ServletContextListenerTest extends AbstractCodegenTestCase {
    public static final String PACKAGE_NAME = "com.atlassian.plugins.servlet.listener";
    private PluginModuleLocation moduleLocation;
    private ServletProperties props;

    @Before
    public void runGenerator() throws Exception {
        PluginModuleCreator creator = pluginModuleCreatorRegistry.getModuleCreator(PluginModuleCreatorRegistry.JIRA, ServletContextListenerModuleCreator.MODULE_NAME);
        moduleLocation = new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build();

        props = new ServletProperties(PACKAGE_NAME + ".MyServletContextListener");
        props.setIncludeExamples(false);

        creator.createModule(moduleLocation, props);
    }

    @Test
    public void allFilesAreGenerated() throws Exception {
        String packagePath = PACKAGE_NAME.replaceAll("\\.", File.separator);
        assertTrue("main class not generated", new File(srcDir, packagePath + File.separator + "MyServletContextListener.java").exists());
        assertTrue("test class not generated", new File(testDir, packagePath + File.separator + "MyServletContextListenerTest.java").exists());
        assertTrue("main class not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void pluginXmlContainsModule() throws IOException {
        String pluginXmlContent = FileUtils.readFileToString(pluginXml);

        assertTrue("module not found in plugin xml", pluginXmlContent.contains("<servlet-context-listener"));
        assertTrue("module class not found in plugin xml", pluginXmlContent.contains("class=\"" + PACKAGE_NAME + ".MyServletContextListener\""));
    }

}
