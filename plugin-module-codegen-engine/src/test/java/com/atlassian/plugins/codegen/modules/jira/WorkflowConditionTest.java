package com.atlassian.plugins.codegen.modules.jira;

import com.atlassian.plugins.codegen.AbstractCodegenTestCase;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import org.dom4j.Document;
import org.dom4j.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @since 3.5
 */
public class WorkflowConditionTest extends AbstractCodegenTestCase<WorkflowElementProperties> {
    public static final String PACKAGE_NAME = "com.atlassian.plugins.workflow";
    public static final String XPATH = "/atlassian-plugin/workflow-condition[@name='My Workflow Condition' and @key='my-workflow-condition' and @i18n-name-key='my-workflow-condition.name' and @class='" + PACKAGE_NAME + ".MyWorkflowConditionFactory']";

    protected File templatePath;

    @Before
    public void runGenerator() throws Exception {
        setCreator(new WorkflowConditionModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new WorkflowElementProperties(PACKAGE_NAME + ".MyWorkflowCondition"));

        props.setIncludeExamples(false);

        templatePath = new File(templateDir,"conditions");

    }

    @Test
    public void allFilesAreGenerated() throws Exception {

        creator.createModule(moduleLocation, props);

        String packagePath = PACKAGE_NAME.replaceAll("\\.", File.separator);

        assertTrue("main class not generated", new File(srcDir, packagePath + File.separator + "MyWorkflowCondition.java").exists());
        assertTrue("factory class not generated", new File(srcDir, packagePath + File.separator + "MyWorkflowConditionFactory.java").exists());
        assertTrue("test class not generated", new File(testDir, packagePath + File.separator + "MyWorkflowConditionTest.java").exists());
        assertTrue("view template not generated", new File(templatePath, "my-workflow-condition.vm").exists());
        assertTrue("input template not generated", new File(templatePath, "my-workflow-condition-input.vm").exists());
        assertTrue("plugin.xml not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void moduleIsValid() throws Exception {

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);
        Node moduleNode = pluginDoc.selectSingleNode(XPATH);

        assertNotNull("valid workflow-condition not found", moduleNode);
    }

    @Test
    public void moduleHasCondition() throws Exception {

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);
        Node moduleNode = pluginDoc.selectSingleNode(XPATH);

        String subXpath = "condition-class[text() = '" + PACKAGE_NAME + ".MyWorkflowCondition']";

        assertNotNull("valid workflow-condition not found", moduleNode);
        assertNotNull("valid condition-class not found", moduleNode.selectSingleNode(subXpath));
    }

}
