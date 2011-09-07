package com.atlassian.plugins.codegen.modules.common.web;

import com.atlassian.plugins.codegen.AbstractCodegenTestCase;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import org.dom4j.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * @since 3.5
 */
public class WebResourceTransformerTest extends AbstractCodegenTestCase<WebResourceTransformerProperties> {
    public static final String PACKAGE_NAME = "com.atlassian.plugin.webresource";

    @Before
    public void runGenerator() throws Exception {
        setCreator(new WebResourceTransformerModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new WebResourceTransformerProperties(PACKAGE_NAME + ".MyWebResourceTransformer"));

        props.setIncludeExamples(false);

        creator.createModule(moduleLocation, props);
    }

    @Test
    public void allFilesAreGenerated() throws Exception {
        String packagePath = PACKAGE_NAME.replaceAll("\\.", File.separator);
        String itPackagePath = "it" + File.separator + packagePath;
        assertTrue("main class not generated", new File(srcDir, packagePath + File.separator + "MyWebResourceTransformer.java").exists());
        assertTrue("test class not generated", new File(testDir, packagePath + File.separator + "MyWebResourceTransformerTest.java").exists());
        assertTrue("plugin.xml not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void moduleIsValid() throws Exception {
        String xpath = "/atlassian-plugin/web-resource-transformer[@name='My Web Resource Transformer' and @key='my-web-resource-transformer' and @i18n-name-key='my-web-resource-transformer.name' and @class='" + PACKAGE_NAME + ".MyWebResourceTransformer']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid web-resource-transformer not found", pluginDoc.selectSingleNode(xpath));
    }

}
