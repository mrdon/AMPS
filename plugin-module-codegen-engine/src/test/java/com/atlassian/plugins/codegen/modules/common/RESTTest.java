package com.atlassian.plugins.codegen.modules.common;

import com.atlassian.plugins.codegen.AbstractCodegenTestCase;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import org.dom4j.Document;
import org.dom4j.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @since version
 */
public class RESTTest extends AbstractCodegenTestCase<RESTProperties> {
    public static final String PACKAGE_NAME = "com.atlassian.plugins.rest";

    @Before
    public void runGenerator() throws Exception {
        setCreator(new RESTModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new RESTProperties(PACKAGE_NAME + ".MyRestResource"));

        props.setIncludeExamples(false);
    }

    @Test
    public void allFilesAreGenerated() throws Exception {
        creator.createModule(moduleLocation, props);
        
        String packagePath = PACKAGE_NAME.replaceAll("\\.", File.separator);
        String itPackagePath = "it" + File.separator + packagePath;
        assertTrue("main class not generated", new File(srcDir, packagePath + File.separator + "MyRestResource.java").exists());
        assertTrue("model class not generated", new File(srcDir, packagePath + File.separator + "MyRestResourceModel.java").exists());
        assertTrue("test class not generated", new File(testDir, packagePath + File.separator + "MyRestResourceTest.java").exists());
        assertTrue("funcTest class not generated", new File(testDir, itPackagePath + File.separator + "MyRestResourceFuncTest.java").exists());
        assertTrue("plugin.xml not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void moduleIsValid() throws Exception {
        String xpath = "/atlassian-plugin/rest[@name='My Rest Resource' and @key='my-rest-resource' and @i18n-name-key='my-rest-resource.name' and @path='/myrestresource' and @version='1.0']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid rest not found", pluginDoc.selectSingleNode(xpath));
    }

    @Test
    public void customPathIsValid() throws Exception {
        String xpath = "/atlassian-plugin/rest[@name='My Rest Resource' and @key='my-rest-resource' and @i18n-name-key='my-rest-resource.name' and @path='/helloworld' and @version='1.0']";

        props.setPath("/helloworld");
        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("rest with custom path not found", pluginDoc.selectSingleNode(xpath));
    }

    @Test
    public void customVersionIsValid() throws Exception {
        String xpath = "/atlassian-plugin/rest[@name='My Rest Resource' and @key='my-rest-resource' and @i18n-name-key='my-rest-resource.name' and @path='/myrestresource' and @version='1.1']";

        props.setVersion("1.1");
        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("rest with custom version not found", pluginDoc.selectSingleNode(xpath));
    }

    @Test
    public void packagesAreAdded() throws Exception {
        String package1 = "com.atlassian.plugins.rest.hello";
        String package2 = "com.atlassian.plugins.rest.message";

        String xpath = "/atlassian-plugin/rest[@name='My Rest Resource' and @key='my-rest-resource' and @i18n-name-key='my-rest-resource.name' and @path='/myrestresource' and @version='1.0']";
        String package1Xpath = "package[text() = '" + package1 + "'";
        String package2Xpath = "package[text() = '" + package2 + "'";

        props.setPackagesToScan(Arrays.asList(package1,package2));
        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        Node restNode = pluginDoc.selectSingleNode(xpath);
        assertNotNull("rest not found", restNode);
        assertNotNull("package 1 not found",restNode.selectSingleNode(package1Xpath));
        assertNotNull("package 2 not found",restNode.selectSingleNode(package2Xpath));
    }

    @Test
    public void dispatchersAreAdded() throws Exception {
        String requestDispatcher = "REQUEST";
        String forwardDispatcher = "FORWARD";

        String xpath = "/atlassian-plugin/rest[@name='My Rest Resource' and @key='my-rest-resource' and @i18n-name-key='my-rest-resource.name' and @path='/myrestresource' and @version='1.0']";
        String dispatcher1Xpath = "dispatcher[text() = '" + requestDispatcher + "'";
        String dispatcher2Xpath = "dispatcher[text() = '" + forwardDispatcher + "'";

        props.setDispatchers(Arrays.asList(requestDispatcher,forwardDispatcher));
        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        Node restNode = pluginDoc.selectSingleNode(xpath);
        assertNotNull("rest not found", restNode);
        assertNotNull("request dispatcher not found",restNode.selectSingleNode(dispatcher1Xpath));
        assertNotNull("forward dispatcher not found",restNode.selectSingleNode(dispatcher2Xpath));
    }

}
