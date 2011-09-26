package com.atlassian.plugins.codegen.modules.jira;

import java.io.File;

import com.atlassian.plugins.codegen.AbstractCodegenTestCase;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;

import org.dom4j.Document;
import org.dom4j.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @since 3.5
 */
public class RPCTest extends AbstractCodegenTestCase<RPCProperties>
{
    public static final String PACKAGE_NAME = "com.atlassian.plugins.jira.rpc";

    @Before
    public void runGenerator() throws Exception
    {
        setCreator(new RPCModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());
    }

    @Test
    public void allSoapFilesAreGenerated() throws Exception
    {
        setProps(new RPCProperties(PACKAGE_NAME + ".MySoapEndpoint"));
        props.setIncludeExamples(false);
        props.setSoap(true);

        creator.createModule(moduleLocation, props);

        String packagePath = PACKAGE_NAME.replaceAll("\\.", File.separator);
        assertTrue("main class not generated", new File(srcDir, packagePath + File.separator + "MySoapEndpointImpl.java").exists());
        assertTrue("interface not generated", new File(srcDir, packagePath + File.separator + "MySoapEndpoint.java").exists());
        assertTrue("test class not generated", new File(testDir, packagePath + File.separator + "MySoapEndpointTest.java").exists());
        assertTrue("plugin.xml not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void allXmlFilesAreGenerated() throws Exception
    {
        setProps(new RPCProperties(PACKAGE_NAME + ".MyXmlEndpoint"));
        props.setIncludeExamples(false);
        props.setSoap(false);

        creator.createModule(moduleLocation, props);

        String packagePath = PACKAGE_NAME.replaceAll("\\.", File.separator);
        assertTrue("main class not generated", new File(srcDir, packagePath + File.separator + "MyXmlEndpointImpl.java").exists());
        assertTrue("interface not generated", new File(srcDir, packagePath + File.separator + "MyXmlEndpoint.java").exists());
        assertTrue("test class not generated", new File(testDir, packagePath + File.separator + "MyXmlEndpointTest.java").exists());
        assertTrue("plugin.xml not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void soapModuleIsValid() throws Exception
    {
        String xpath = "/atlassian-plugin/rpc-soap[@name='My Soap Endpoint' and @key='my-soap-endpoint' and @i18n-name-key='my-soap-endpoint.name' and @class='" + PACKAGE_NAME + ".MySoapEndpointImpl']";
        String pathXPath = "service-path[text() = 'mysoapendpoint-v1']";
        String interfaceXPath = "published-interface[text() = '" + PACKAGE_NAME + ".MySoapEndpoint']";

        setProps(new RPCProperties(PACKAGE_NAME + ".MySoapEndpoint"));
        props.setIncludeExamples(false);
        props.setSoap(true);

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        Node rpcNode = pluginDoc.selectSingleNode(xpath);

        assertNotNull("valid rpc-soap not found", rpcNode);
        assertNotNull("valid service-path not found", rpcNode.selectSingleNode(pathXPath));
        assertNotNull("valid published-interface not found", rpcNode.selectSingleNode(interfaceXPath));
    }

    @Test
    public void xmlModuleIsValid() throws Exception
    {
        String xpath = "/atlassian-plugin/rpc-xmlrpc[@name='My Xml Endpoint' and @key='my-xml-endpoint' and @i18n-name-key='my-xml-endpoint.name' and @class='" + PACKAGE_NAME + ".MyXmlEndpointImpl']";

        String pathXPath = "service-path[text() = 'myxmlendpoint-v1']";

        setProps(new RPCProperties(PACKAGE_NAME + ".MyXmlEndpoint"));
        props.setIncludeExamples(false);
        props.setSoap(false);

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        Node rpcNode = pluginDoc.selectSingleNode(xpath);

        assertNotNull("valid rpc-soap not found", rpcNode);
        assertNotNull("valid service-path not found", rpcNode.selectSingleNode(pathXPath));
    }

    @Test
    public void soapComponentAdded() throws Exception
    {
        setProps(new RPCProperties(PACKAGE_NAME + ".MySoapEndpoint"));
        props.setIncludeExamples(false);
        props.setSoap(true);

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        String compXPath = "/atlassian-plugin/component[@name='My Soap Endpoint Component' and @key='my-soap-endpoint-component' and @i18n-name-key='my-soap-endpoint.name.component' and @class='" + PACKAGE_NAME + ".MySoapEndpointImpl']";
        String compIfaceXPath = "interface[text() = '" + PACKAGE_NAME + ".MySoapEndpoint']";

        Node compNode = pluginDoc.selectSingleNode(compXPath);
        assertNotNull("component not found", compNode);
        assertNotNull("interface not found", compNode.selectSingleNode(compIfaceXPath));

    }

    @Test
    public void xmlComponentAdded() throws Exception
    {
        setProps(new RPCProperties(PACKAGE_NAME + ".MyXmlEndpoint"));
        props.setIncludeExamples(false);
        props.setSoap(true);

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        String compXPath = "/atlassian-plugin/component[@name='My Xml Endpoint Component' and @key='my-xml-endpoint-component' and @i18n-name-key='my-xml-endpoint.name.component' and @class='" + PACKAGE_NAME + ".MyXmlEndpointImpl']";
        String compIfaceXPath = "interface[text() = '" + PACKAGE_NAME + ".MyXmlEndpoint']";

        Node compNode = pluginDoc.selectSingleNode(compXPath);
        assertNotNull("component not found", compNode);
        assertNotNull("interface not found", compNode.selectSingleNode(compIfaceXPath));
    }
}
