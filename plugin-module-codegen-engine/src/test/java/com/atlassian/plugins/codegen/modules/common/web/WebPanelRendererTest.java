package com.atlassian.plugins.codegen.modules.common.web;

import java.io.File;

import com.atlassian.plugins.codegen.AbstractCodegenTestCase;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;

import org.dom4j.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @since 3.6
 */
public class WebPanelRendererTest extends AbstractCodegenTestCase<WebPanelRendererProperties>
{
    public static final String PACKAGE_NAME = "com.atlassian.plugins.web";

    @Before
    public void runGenerator() throws Exception
    {
        setCreator(new WebPanelRendererModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new WebPanelRendererProperties(PACKAGE_NAME + ".MyWebPanelRenderer"));

        props.setIncludeExamples(false);

        creator.createModule(moduleLocation, props);
    }

    @Test
    public void allFilesAreGenerated() throws Exception
    {
        String packagePath = PACKAGE_NAME.replaceAll("\\.", File.separator);
        assertTrue("main class not generated", new File(srcDir, packagePath + File.separator + "MyWebPanelRenderer.java").exists());
        assertTrue("test class not generated", new File(testDir, packagePath + File.separator + "MyWebPanelRendererTest.java").exists());
        assertTrue("plugin.xml not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void moduleIsValid() throws Exception
    {
        String xpath = "/atlassian-plugin/web-panel-renderer[@name='My Web Panel Renderer' and @key='my-web-panel-renderer' and @i18n-name-key='my-web-panel-renderer.name' and @class='" + PACKAGE_NAME + ".MyWebPanelRenderer']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid web-panel-renderer not found", pluginDoc.selectSingleNode(xpath));
    }


}
