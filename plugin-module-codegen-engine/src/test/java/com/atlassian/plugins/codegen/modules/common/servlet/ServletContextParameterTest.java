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
//TODO: update test to use Dom4J
/**
 * @since version
 */
public class ServletContextParameterTest extends AbstractCodegenTestCase {
    private PluginModuleLocation moduleLocation;
    private ServletContextParameterProperties props;

    @Before
    public void runGenerator() throws Exception {
        ServletContextParameterModuleCreator creator = pluginModuleCreatorRegistry.getModuleCreator(PluginModuleCreatorRegistry.JIRA, ServletContextParameterModuleCreator.class);
        moduleLocation = new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build();

        props = new ServletContextParameterProperties("MY Param Name");

        creator.createModule(moduleLocation, props);
    }

    @Test
    public void pluginXmlContainsModule() throws IOException {
        String pluginXmlContent = FileUtils.readFileToString(pluginXml);

        assertTrue("module not found in plugin xml", pluginXmlContent.contains("<servlet-context-param"));
    }

}
