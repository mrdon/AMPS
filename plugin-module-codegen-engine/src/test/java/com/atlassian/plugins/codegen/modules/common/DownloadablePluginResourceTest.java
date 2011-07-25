package com.atlassian.plugins.codegen.modules.common;

import com.atlassian.plugins.codegen.AbstractCodegenTestCase;
import com.atlassian.plugins.codegen.modules.PluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleCreatorRegistry;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * @since version
 */
public class DownloadablePluginResourceTest extends AbstractCodegenTestCase<DownloadablePluginResourceProperties> {

    public static final String XPATH_RESOURCE = "/atlassian-plugin/resource";
    public static final String XPATH_PARAM_RELATIVE = "param";

    /*
        The base module creator auto-adds an i18n resource, and so all of our tests need to add 1 to their counts
     */

    @Before
    public void setupCreator() throws Exception {
        setCreator(new DownloadablePluginResourceModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());
    }

    @Test
    public void singleResourceAdded() throws Exception {
        Resource resource = new Resource();
        resource.setName("style.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");

        setProps(new DownloadablePluginResourceProperties(resource));
        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        List<Node> resourceList = pluginDoc.selectNodes(XPATH_RESOURCE);

        assertEquals("expected single resource",2,resourceList.size());

        String nodeXpath = "//resource[@name='style.css' and @location='com/example/plugin/style.css' and @type='download']";
        assertNotNull("single resource not found", pluginDoc.selectSingleNode(nodeXpath));

    }

    @Test
    public void singleResourceNamePatternAdded() throws Exception {
        Resource resource = new Resource();
        resource.setNamePattern("*.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");

        setProps(new DownloadablePluginResourceProperties(resource));
        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        List<Node> resourceList = pluginDoc.selectNodes(XPATH_RESOURCE);

        assertEquals("expected single resource",2,resourceList.size());

        String nodeXpath = "//resource[@namePattern='*.css' and @location='com/example/plugin/style.css' and @type='download']";
        assertNotNull("single resource not found",pluginDoc.selectSingleNode(nodeXpath));

    }

    @Test
    public void nameChosenOverPattern() throws Exception {
        Resource resource = new Resource();
        resource.setName("style.css");
        resource.setNamePattern("*.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");

        setProps(new DownloadablePluginResourceProperties(resource));
        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        List<Node> resourceList = pluginDoc.selectNodes(XPATH_RESOURCE);

        assertEquals("expected single resource",2,resourceList.size());

        String nodeXpath = "//resource[not(@namePattern) and @name='style.css' and @location='com/example/plugin/style.css' and @type='download']";
        assertNotNull("single resource not found",pluginDoc.selectSingleNode(nodeXpath));

    }

    @Test
    public void resourceParamsAdded() throws Exception {
        Resource resource = new Resource();
        resource.setName("style.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");
        resource.getParams().put("content-type","text/css");
        resource.getParams().put("awesome","me");

        setProps(new DownloadablePluginResourceProperties(resource));
        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        List<Node> resourceList = pluginDoc.selectNodes(XPATH_RESOURCE);

        assertEquals("expected single resource",2,resourceList.size());

        String nodeXpath = "//resource[not(@namePattern) and @name='style.css' and @location='com/example/plugin/style.css' and @type='download']";
        Node resourceNode = pluginDoc.selectSingleNode(nodeXpath);

        List<Node> paramList = resourceNode.selectNodes(XPATH_PARAM_RELATIVE);
        assertEquals("expected resource params", 2, paramList.size());

        assertNotNull("missing content param",resourceNode.selectSingleNode("param[@name='content-type' and @value='text/css']"));
        assertNotNull("missing awesome param",resourceNode.selectSingleNode("param[@name='awesome' and @value='me']"));

    }


}
