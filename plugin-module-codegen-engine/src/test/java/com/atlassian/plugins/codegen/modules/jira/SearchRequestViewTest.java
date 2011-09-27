package com.atlassian.plugins.codegen.modules.jira;

import java.io.File;
import java.util.List;

import com.atlassian.plugins.codegen.AbstractCodegenTestCase;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.common.Resource;

import org.dom4j.Document;
import org.dom4j.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @since 3.6
 */
public class SearchRequestViewTest extends AbstractCodegenTestCase<SearchRequestViewProperties>
{
    public static final String PACKAGE_NAME = "com.atlassian.plugins.jira.search";
    public static final String EXT = "html";
    public static final String CTYPE = "text/html";
    public static final String XPATH_RESOURCE = "/atlassian-plugin/*//resource";
    public static final String XPATH_PARAM_RELATIVE = "param";


    @Before
    public void runGenerator() throws Exception
    {
        setCreator(new SearchRequestViewModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new SearchRequestViewProperties(PACKAGE_NAME + ".MySearchRequestView"));

        props.setIncludeExamples(false);

    }

    @Test
    public void allFilesAreGenerated() throws Exception
    {
        creator.createModule(moduleLocation, props);

        String packagePath = PACKAGE_NAME.replaceAll("\\.", File.separator);

        assertTrue("main class not generated", new File(srcDir, packagePath + File.separator + "MySearchRequestView.java").exists());
        assertTrue("test class not generated", new File(testDir, packagePath + File.separator + "MySearchRequestViewTest.java").exists());
        assertTrue("plugin.xml not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void moduleIsValid() throws Exception
    {
        props.setFileExtension(EXT);
        props.setContentType(CTYPE);

        String xpath = "/atlassian-plugin/search-request-view[@name='My Search Request View' and @key='my-search-request-view' and @i18n-name-key='my-search-request-view.name' and @class='" + PACKAGE_NAME + ".MySearchRequestView' and @fileExtension='html' and @contentType='text/html']";
        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid search-request-view not found", pluginDoc.selectSingleNode(xpath));

    }

    @Test
    public void singleResourceAdded() throws Exception
    {
        Resource resource = new Resource();
        resource.setName("style.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");

        props.getResources()
                .add(resource);

        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        List<Node> resourceList = pluginDoc.selectNodes(XPATH_RESOURCE);

        assertEquals("expected single resource", 1, resourceList.size());

        String nodeXpath = "//resource[@name='style.css' and @location='com/example/plugin/style.css' and @type='download']";
        assertNotNull("single resource not found", pluginDoc.selectSingleNode(nodeXpath));

    }

    @Test
    public void singleResourceNamePatternAdded() throws Exception
    {
        Resource resource = new Resource();
        resource.setNamePattern("*.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");

        props.getResources()
                .add(resource);

        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        List<Node> resourceList = pluginDoc.selectNodes(XPATH_RESOURCE);

        assertEquals("expected single resource", 1, resourceList.size());

        String nodeXpath = "//resource[@namePattern='*.css' and @location='com/example/plugin/style.css' and @type='download']";
        assertNotNull("single resource not found", pluginDoc.selectSingleNode(nodeXpath));

    }

    @Test
    public void nameChosenOverPattern() throws Exception
    {
        Resource resource = new Resource();
        resource.setName("style.css");
        resource.setNamePattern("*.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");

        props.getResources()
                .add(resource);

        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        List<Node> resourceList = pluginDoc.selectNodes(XPATH_RESOURCE);

        assertEquals("expected single resource", 1, resourceList.size());

        String nodeXpath = "//resource[not(@namePattern) and @name='style.css' and @location='com/example/plugin/style.css' and @type='download']";
        assertNotNull("single resource not found", pluginDoc.selectSingleNode(nodeXpath));

    }

    @Test
    public void resourceParamsAdded() throws Exception
    {
        Resource resource = new Resource();
        resource.setName("style.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");
        resource.getParams()
                .put("content-type", "text/css");
        resource.getParams()
                .put("awesome", "me");

        props.getResources()
                .add(resource);

        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        List<Node> resourceList = pluginDoc.selectNodes(XPATH_RESOURCE);

        assertEquals("expected single resource", 1, resourceList.size());

        String nodeXpath = "//resource[not(@namePattern) and @name='style.css' and @location='com/example/plugin/style.css' and @type='download']";
        Node resourceNode = pluginDoc.selectSingleNode(nodeXpath);

        List<Node> paramList = resourceNode.selectNodes(XPATH_PARAM_RELATIVE);
        assertEquals("expected resource params", 2, paramList.size());

        assertNotNull("missing content param", resourceNode.selectSingleNode("param[@name='content-type' and @value='text/css']"));
        assertNotNull("missing awesome param", resourceNode.selectSingleNode("param[@name='awesome' and @value='me']"));

    }

    @Test
    public void multipleResourcesAdded() throws Exception
    {
        Resource resource = new Resource();
        resource.setName("style.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");
        resource.getParams()
                .put("content-type", "text/css");
        resource.getParams()
                .put("awesome", "me");

        Resource resource2 = new Resource();
        resource2.setName("custom.js");
        resource2.setLocation("com/example/plugin/custom.js");
        resource2.setType("download");

        props.getResources()
                .add(resource);
        props.getResources()
                .add(resource2);

        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        List<Node> resourceList = pluginDoc.selectNodes(XPATH_RESOURCE);

        assertEquals("expected multiple resources", 2, resourceList.size());

        String nodeXpath = "//resource[not(@namePattern) and @name='style.css' and @location='com/example/plugin/style.css' and @type='download']";
        String node2Xpath = "//resource[not(@namePattern) and @name='custom.js' and @location='com/example/plugin/custom.js' and @type='download']";

        assertNotNull("missing css resource", pluginDoc.selectSingleNode(nodeXpath));
        assertNotNull("missing js resource", pluginDoc.selectSingleNode(node2Xpath));

    }

}
