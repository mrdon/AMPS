package com.atlassian.plugins.codegen.modules.common.web;

import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.common.AbstractConditionTest;
import com.atlassian.plugins.codegen.modules.common.Resource;
import org.dom4j.Document;
import org.dom4j.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @since version
 */
public class WebResourceTest extends AbstractConditionTest<WebResourceProperties> {
    public static final String MODULE_NAME = "Awesome Web Resource";
    public static final String XPATH_RESOURCE = "/atlassian-plugin/web-resource/resource";

    @Before
    public void runGenerator() throws Exception {
        setCreator(new WebResourceModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new WebResourceProperties(MODULE_NAME));

        props.setIncludeExamples(false);

    }

    @Test
    public void moduleIsValid() throws Exception {
        String xpath = "/atlassian-plugin/web-resource[@name='Awesome Web Resource' and @key='awesome-web-resource' and @i18n-name-key='awesome-web-resource.name']";

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);

        assertNotNull("valid web-resource not found", pluginDoc.selectSingleNode(xpath));
    }

    @Test
    public void dependenciesAreAdded() throws Exception {
        Resource resource = new Resource();
        resource.setName("style.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");

        props.getResources().add(resource);

        props.addDependency("jira.web.resources:ajs");
        props.addDependency("jira.web.resources:jquery");

        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);

        String depXPath = "/atlassian-plugin/web-resource/dependency";
        List<Node> depList = pluginDoc.selectNodes(depXPath);

        assertEquals("expected multiple dependencies", 2, depList.size());
        assertNotNull("missing ajs dependency", pluginDoc.selectSingleNode("/atlassian-plugin/web-resource/dependency[text() = 'jira.web.resources:ajs']"));
        assertNotNull("missing jquery dependency", pluginDoc.selectSingleNode("/atlassian-plugin/web-resource/dependency[text() = 'jira.web.resources:jquery']"));

    }

    @Test
    public void contextsAreAdded() throws Exception {
        Resource resource = new Resource();
        resource.setName("style.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");

        props.getResources().add(resource);

        props.addContext("atl.general");
        props.addContext("atl.userprofile");

        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);

        String contextXPath = "/atlassian-plugin/web-resource/context";
        List<Node> contextList = pluginDoc.selectNodes(contextXPath);

        assertEquals("expected multiple dependencies", 2, contextList.size());
        assertNotNull("missing general context", pluginDoc.selectSingleNode("/atlassian-plugin/web-resource/context[text() = 'atl.general']"));
        assertNotNull("missing userprofile context", pluginDoc.selectSingleNode("/atlassian-plugin/web-resource/context[text() = 'atl.userprofile']"));

    }

    @Test
    public void singleTransformationWithSingleTransformerAdded() throws Exception {
        Resource resource = new Resource();
        resource.setName("style.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");

        props.getResources().add(resource);

        WebResourceTransformation transformation = new WebResourceTransformation("txt");
        transformation.addTransformerKey("template");

        props.addTransformation(transformation);

        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);

        String transformationXPath = "/atlassian-plugin/web-resource/transformation";
        String transformerXPath = "transformer";

        List<Node> transformationList = pluginDoc.selectNodes(transformationXPath);

        assertEquals("wrong number of transformations",1,transformationList.size());

        Node transformationNode = transformationList.get(0);
        assertEquals("wrong number of transformers",1,transformationNode.selectNodes(transformerXPath).size());
        assertEquals("wrong transformation extension","txt",transformationNode.selectSingleNode("@extension").getStringValue());
        assertNotNull("template transformer not found",transformationNode.selectSingleNode("transformer[@key='template']"));
    }

    @Test
    public void singleTransformationWithMultipleTransformersAdded() throws Exception {
        Resource resource = new Resource();
        resource.setName("style.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");

        props.getResources().add(resource);

        WebResourceTransformation transformation = new WebResourceTransformation("txt");
        transformation.addTransformerKey("template");
        transformation.addTransformerKey("prefix");

        props.addTransformation(transformation);

        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);

        String transformationXPath = "/atlassian-plugin/web-resource/transformation";
        String transformerXPath = "transformer";

        List<Node> transformationList = pluginDoc.selectNodes(transformationXPath);

        assertEquals("wrong number of transformations",1,transformationList.size());

        Node transformationNode = transformationList.get(0);

        assertEquals("wrong number of transformers",2,transformationNode.selectNodes(transformerXPath).size());
        assertEquals("wrong transformation extension","txt",transformationNode.selectSingleNode("@extension").getStringValue());
        assertNotNull("template transformer not found",transformationNode.selectSingleNode("transformer[@key='template']"));
        assertNotNull("prefix transformer not found",transformationNode.selectSingleNode("transformer[@key='prefix']"));
    }

    @Test
    public void multipleTransformationsAdded() throws Exception {
        Resource resource = new Resource();
        resource.setName("style.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");

        props.getResources().add(resource);

        WebResourceTransformation txtTrans = new WebResourceTransformation("txt");
        txtTrans.addTransformerKey("template");

        WebResourceTransformation cssTrans = new WebResourceTransformation("css");
        cssTrans.addTransformerKey("prefix");

        props.addTransformation(txtTrans);
        props.addTransformation(cssTrans);

        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);

        String transformationXPath = "/atlassian-plugin/web-resource/transformation";
        String transformerXPath = "transformer";

        List<Node> transformationList = pluginDoc.selectNodes(transformationXPath);

        assertEquals("wrong number of transformations",2,transformationList.size());

        Node txtNode = pluginDoc.selectSingleNode(transformationXPath + "[@extension='txt']");
        Node cssNode = pluginDoc.selectSingleNode(transformationXPath + "[@extension='css']");

        assertNotNull("missing txt node",txtNode);
        assertNotNull("missing css node",cssNode);

        assertNotNull("template transformer not found",txtNode.selectSingleNode("transformer[@key='template']"));
        assertNotNull("prefix transformer not found",cssNode.selectSingleNode("transformer[@key='prefix']"));
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
    public void nonBatchResourceParamAdded() throws Exception {
        Resource resource = new Resource();
        resource.setName("style.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");
        resource.setBatch(false);

        props.getResources().add(resource);

        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        List<Node> resourceList = pluginDoc.selectNodes(XPATH_RESOURCE);

        assertEquals("expected single resource", 1, resourceList.size());

        String nodeXpath = "//resource[not(@namePattern) and @name='style.css' and @location='com/example/plugin/style.css' and @type='download']";
        Node resourceNode = pluginDoc.selectSingleNode(nodeXpath);

        List<Node> paramList = resourceNode.selectNodes(XPATH_PARAM_RELATIVE);
        assertEquals("expected resource batch param", 1, paramList.size());

        assertNotNull("missing batch param", resourceNode.selectSingleNode("param[@name='batch' and @value='false']"));

    }

    @Test
    public void nonBatchResourceWithParamsAdded() throws Exception {
        Resource resource = new Resource();
        resource.setName("style.css");
        resource.setLocation("com/example/plugin/style.css");
        resource.setType("download");
        resource.setBatch(false);
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
        assertEquals("expected resource params", 3, paramList.size());

        assertNotNull("missing batch param", resourceNode.selectSingleNode("param[@name='batch' and @value='false']"));
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
