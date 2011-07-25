package com.atlassian.plugins.codegen.modules.common.web;

import com.atlassian.plugins.codegen.modules.common.AbstractConditionTest;
import com.atlassian.plugins.codegen.modules.common.Resource;
import org.dom4j.Document;
import org.dom4j.Node;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @since version
 */

public abstract class AbstractWebFragmentTest<T extends AbstractWebFragmentProperties> extends AbstractConditionTest<T> {

    public static final String JIRA_MODIFIER_KEY_CTX_PROVIDER = "com.atlassian.jira.plugin.webfragment.contextproviders.ModifierKeyContextProvider";
    public static final String XPATH_CTX_PROVIDER = "//context-provider";
    public static final String XPATH_RESOURCE = "/atlassian-plugin/*//resource";
    public static final String XPATH_PARAM_RELATIVE = "param";

    @Test
    public void contextProviderIsAdded() throws Exception {
        props.setContextProvider(JIRA_MODIFIER_KEY_CTX_PROVIDER);
        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);

        List<Node> ctxProviderList = pluginDoc.selectNodes(XPATH_CTX_PROVIDER);
        assertEquals("wrong number of context providers", 1, ctxProviderList.size());

        Node ctxProviderNode = ctxProviderList.get(0);
        assertEquals("wrong context provider class", JIRA_MODIFIER_KEY_CTX_PROVIDER, ctxProviderNode.selectSingleNode("@class").getStringValue());
    }

    @Test
    public void singleResourceAdded() throws Exception {
        Resource resource = new Resource();
        resource.setName("style.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");

        props.getResources().add(resource);

        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        List<Node> resourceList = pluginDoc.selectNodes(XPATH_RESOURCE);

        assertEquals("expected single resource", 1, resourceList.size());

        String nodeXpath = "//resource[@name='style.css' and @location='com/example/plugin/style.css' and @type='download']";
        assertNotNull("single resource not found", pluginDoc.selectSingleNode(nodeXpath));

    }

    @Test
    public void singleResourceNamePatternAdded() throws Exception {
        Resource resource = new Resource();
        resource.setNamePattern("*.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");

        props.getResources().add(resource);

        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        List<Node> resourceList = pluginDoc.selectNodes(XPATH_RESOURCE);

        assertEquals("expected single resource", 1, resourceList.size());

        String nodeXpath = "//resource[@namePattern='*.css' and @location='com/example/plugin/style.css' and @type='download']";
        assertNotNull("single resource not found", pluginDoc.selectSingleNode(nodeXpath));

    }

    @Test
    public void nameChosenOverPattern() throws Exception {
        Resource resource = new Resource();
        resource.setName("style.css");
        resource.setNamePattern("*.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");

        props.getResources().add(resource);

        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        List<Node> resourceList = pluginDoc.selectNodes(XPATH_RESOURCE);

        assertEquals("expected single resource", 1, resourceList.size());

        String nodeXpath = "//resource[not(@namePattern) and @name='style.css' and @location='com/example/plugin/style.css' and @type='download']";
        assertNotNull("single resource not found", pluginDoc.selectSingleNode(nodeXpath));

    }

    @Test
    public void resourceParamsAdded() throws Exception {
        Resource resource = new Resource();
        resource.setName("style.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");
        resource.getParams().put("content-type", "text/css");
        resource.getParams().put("awesome", "me");

        props.getResources().add(resource);

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
    public void multipleResourcesAdded() throws Exception {
        Resource resource = new Resource();
        resource.setName("style.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");
        resource.getParams().put("content-type", "text/css");
        resource.getParams().put("awesome", "me");

        Resource resource2 = new Resource();
        resource2.setName("custom.js");
        resource2.setLocation("com/example/plugin/custom.js");
        resource2.setType("download");

        props.getResources().add(resource);
        props.getResources().add(resource2);

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
