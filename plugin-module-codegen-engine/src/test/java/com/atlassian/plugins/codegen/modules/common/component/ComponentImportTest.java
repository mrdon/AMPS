package com.atlassian.plugins.codegen.modules.common.component;

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
public class ComponentImportTest extends AbstractCodegenTestCase {
    private PluginModuleLocation moduleLocation;
    private ComponentImportProperties props;

    @Before
    public void runGenerator() throws Exception {
        ComponentImportModuleCreator creator = pluginModuleCreatorRegistry.getModuleCreator(PluginModuleCreatorRegistry.JIRA, ComponentImportModuleCreator.class);
        moduleLocation = new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build();

        props = new ComponentImportProperties("com.atlassian.SomeInterface");
        props.setIncludeExamples(false);

        creator.createModule(moduleLocation, props);
    }


    @Test
    public void pluginXmlContainsModule() throws IOException {
        String pluginXmlContent = FileUtils.readFileToString(pluginXml);

        assertTrue("module not found in plugin xml", pluginXmlContent.contains("<component-import"));
        assertTrue("module class not found in plugin xml", pluginXmlContent.contains("interface=\"com.atlassian.SomeInterface\""));
    }

}
