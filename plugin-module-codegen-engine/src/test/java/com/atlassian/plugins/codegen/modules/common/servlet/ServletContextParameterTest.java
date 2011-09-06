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
 * @since 3.5
 */
public class ServletContextParameterTest extends AbstractCodegenTestCase<ServletContextParameterProperties> {

    @Before
    public void runGenerator() throws Exception {
        setCreator(new ServletContextParameterModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new ServletContextParameterProperties("MY Param Name"));

        creator.createModule(moduleLocation, props);
    }

    @Test
    public void pluginXmlContainsModule() throws IOException {
        String pluginXmlContent = FileUtils.readFileToString(pluginXml);

        assertTrue("module not found in plugin xml", pluginXmlContent.contains("<servlet-context-param"));
    }

}
