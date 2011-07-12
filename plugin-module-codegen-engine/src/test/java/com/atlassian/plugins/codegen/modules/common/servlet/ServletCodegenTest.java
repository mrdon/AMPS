package com.atlassian.plugins.codegen.modules.common.servlet;

import com.atlassian.plugins.codegen.AbstractCodegenTestCase;
import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;
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
public class ServletCodegenTest extends AbstractCodegenTestCase {
    public static final String PACKAGE_NAME = "com.atlassian.plugins.servlet";
    private PluginModuleLocation moduleLocation;
    private ServletProperties props;

    @Before
    public void runGenerator() throws Exception {
        ServletModuleCreator creator = pluginModuleCreatorRegistry.getModuleCreator(PluginModuleCreatorRegistry.JIRA, ServletModuleCreator.class);
        moduleLocation = new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build();

        props = new ServletProperties(PACKAGE_NAME + ".MyServlet");
        props.setUrlPattern("/myservlet");
        props.setIncludeExamples(false);
        props.addInitParam("foo", "bar");

        creator.createModule(moduleLocation,props);
        creator.createModule(moduleLocation,props);
    }

    @Test
    public void allFilesAreGenerated() throws Exception {
        String packagePath = PACKAGE_NAME.replaceAll("\\.",File.separator);
        String itPackagePath = "it" + File.separator + packagePath;
        assertTrue("main class not generated", new File(srcDir,packagePath + File.separator + "MyServlet.java").exists());
        assertTrue("test class not generated", new File(testDir,packagePath + File.separator + "MyServletTest.java").exists());
        assertTrue("funcTest class not generated", new File(testDir,itPackagePath + File.separator + "MyServletFuncTest.java").exists());
        assertTrue("main class not generated", new File(resourcesDir,"atlassian-plugin.xml").exists());

    }

    @Test
    public void pluginXmlContainsModule() throws IOException {
        String pluginXmlContent = FileUtils.readFileToString(pluginXml);

        assertTrue("module not found in plugin xml",pluginXmlContent.contains("<servlet"));
        assertTrue("module class not found in plugin xml", pluginXmlContent.contains("class=\"" + PACKAGE_NAME + ".MyServlet\""));
    }

}
