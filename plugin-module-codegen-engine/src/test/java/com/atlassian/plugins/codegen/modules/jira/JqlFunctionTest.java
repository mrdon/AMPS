package com.atlassian.plugins.codegen.modules.jira;

import java.io.File;
import java.util.regex.Matcher;

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
public class JqlFunctionTest extends AbstractCodegenTestCase<JqlFunctionProperties>
{
    public static final String PACKAGE_NAME = "com.atlassian.plugins.jira.jql";

    @Before
    public void runGenerator() throws Exception
    {
        setCreator(new JqlFunctionModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new JqlFunctionProperties(PACKAGE_NAME + ".MyJqlFunction"));

        props.setIncludeExamples(false);

        creator.createModule(moduleLocation, props);
    }

    @Test
    public void allFilesAreGenerated() throws Exception
    {
        String packagePath = PACKAGE_NAME.replaceAll("\\.", Matcher.quoteReplacement(File.separator));
        String itPackagePath = "it" + File.separator + packagePath;
        assertTrue("main class not generated", new File(srcDir, packagePath + File.separator + "MyJqlFunction.java").exists());
        assertTrue("test class not generated", new File(testDir, packagePath + File.separator + "MyJqlFunctionTest.java").exists());
        assertTrue("plugin.xml not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void moduleIsValid() throws Exception
    {
        String xpath = "/atlassian-plugin/jql-function[@name='My Jql Function' and @key='my-jql-function' and @i18n-name-key='my-jql-function.name' and @class='" + PACKAGE_NAME + ".MyJqlFunction']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid jql-function not found", pluginDoc.selectSingleNode(xpath));
    }

}
