package com.atlassian.plugins.codegen.modules.jira;

import com.atlassian.plugins.codegen.AbstractCodegenTestCase;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import org.dom4j.Document;
import org.dom4j.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * @since version
 */
public class WorkflowPostFunctionTest extends AbstractCodegenTestCase<WorkflowPostFunctionProperties> {
    public static final String PACKAGE_NAME = "com.atlassian.plugins.workflow";
    public static final String XPATH = "/atlassian-plugin/workflow-function[@name='My Post Function' and @key='my-post-function' and @i18n-name-key='my-post-function.name' and @class='" + PACKAGE_NAME + ".MyPostFunctionFactory']";
    @Before
    public void runGenerator() throws Exception {
        setCreator(new WorkflowPostFunctionModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new WorkflowPostFunctionProperties(PACKAGE_NAME + ".MyPostFunction"));

        props.setIncludeExamples(false);

    }

    @Test
    public void allFilesAreGenerated() throws Exception {
        
        creator.createModule(moduleLocation, props);

        String packagePath = PACKAGE_NAME.replaceAll("\\.", File.separator);

        assertTrue("main class not generated", new File(srcDir, packagePath + File.separator + "MyPostFunction.java").exists());
        assertTrue("factory class not generated", new File(srcDir, packagePath + File.separator + "MyPostFunctionFactory.java").exists());
        assertTrue("test class not generated", new File(testDir, packagePath + File.separator + "MyPostFunctionTest.java").exists());
        assertTrue("view template not generated", new File(templateDir, "my-post-function.vm").exists());
        assertTrue("input template not generated", new File(templateDir, "my-post-function-input.vm").exists());
        assertTrue("plugin.xml not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void moduleIsValid() throws Exception {

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);
        Node workflowFunction = pluginDoc.selectSingleNode(XPATH);

        assertNotNull("valid workflow-function not found", workflowFunction);
        assertNull("found orderable but should be null", workflowFunction.selectSingleNode("orderable"));
        assertNull("found unique but should be null", workflowFunction.selectSingleNode("unique"));
        assertNull("found deletable but should be null", workflowFunction.selectSingleNode("deletable"));
        assertNull("found addable but should be null", workflowFunction.selectSingleNode("addable"));
    }

    @Test
    public void moduleHasFunction() throws Exception {

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);
        Node workflowFunction = pluginDoc.selectSingleNode(XPATH);

        String functionXpath = "function-class[text() = '" + PACKAGE_NAME + ".MyPostFunction']";

        assertNotNull("valid workflow-function not found", workflowFunction);
        assertNotNull("valid function-class not found", workflowFunction.selectSingleNode(functionXpath));
    }

    @Test
    public void moduleHasOrderable() throws Exception {

        props.setOrderable(true);
        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);
        Node workflowFunction = pluginDoc.selectSingleNode(XPATH);

        String subXpath = "orderable[text() = 'true']";

        assertNotNull("valid workflow-function not found", workflowFunction);
        assertNotNull("valid orderable not found", workflowFunction.selectSingleNode(subXpath));
    }

    @Test
    public void moduleHasDeletable() throws Exception {

        props.setDeletable(false);
        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);
        Node workflowFunction = pluginDoc.selectSingleNode(XPATH);

        String subXpath = "deletable[text() = 'false']";

        assertNotNull("valid workflow-function not found", workflowFunction);
        assertNotNull("valid deletable not found", workflowFunction.selectSingleNode(subXpath));
    }

    @Test
    public void moduleHasUnique() throws Exception {

        props.setUnique(true);
        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);
        Node workflowFunction = pluginDoc.selectSingleNode(XPATH);

        String subXpath = "unique[text() = 'true']";

        assertNotNull("valid workflow-function not found", workflowFunction);
        assertNotNull("valid unique not found", workflowFunction.selectSingleNode(subXpath));
    }

    @Test
    public void moduleHasAddable() throws Exception {

        props.setAddable("global,common");
        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);
        Node workflowFunction = pluginDoc.selectSingleNode(XPATH);

        String subXpath = "addable[text() = 'global,common']";

        assertNotNull("valid workflow-function not found", workflowFunction);
        assertNotNull("valid addable not found", workflowFunction.selectSingleNode(subXpath));
    }

    @Test
    public void moduleHasMultipleFlags() throws Exception {

        props.setAddable("global,common");
        props.setDeletable(false);
        props.setUnique(true);

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);
        Node workflowFunction = pluginDoc.selectSingleNode(XPATH);

        String addable = "addable[text() = 'global,common']";
        String deletable = "deletable[text() = 'false']";
        String unique = "unique[text() = 'true']";

        assertNotNull("valid workflow-function not found", workflowFunction);
        assertNotNull("valid addable not found", workflowFunction.selectSingleNode(addable));
        assertNotNull("valid unique not found", workflowFunction.selectSingleNode(unique));
        assertNotNull("valid deletable not found", workflowFunction.selectSingleNode(deletable));
    }

}
