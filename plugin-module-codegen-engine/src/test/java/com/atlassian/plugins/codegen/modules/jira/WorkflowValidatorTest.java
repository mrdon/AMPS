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
 * @since 3.5
 */
public class WorkflowValidatorTest extends AbstractCodegenTestCase<WorkflowElementProperties> {
    public static final String PACKAGE_NAME = "com.atlassian.plugins.jira.workflow";
    public static final String XPATH = "/atlassian-plugin/workflow-validator[@name='My Workflow Validator' and @key='my-workflow-validator' and @i18n-name-key='my-workflow-validator.name' and @class='" + PACKAGE_NAME + ".MyWorkflowValidatorFactory']";

        protected File templatePath;


    @Before
    public void runGenerator() throws Exception {
        setCreator(new WorkflowValidatorModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new WorkflowElementProperties(PACKAGE_NAME + ".MyWorkflowValidator"));

        props.setIncludeExamples(false);

        templatePath = new File(templateDir,"validators");
    }

    @Test
    public void moduleIsValid() throws Exception {

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);
        Node moduleNode = pluginDoc.selectSingleNode(XPATH);

        assertNotNull("valid workflow-validator not found", moduleNode);
    }

    @Test
    public void moduleHasCondition() throws Exception {

        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);
        Node moduleNode = pluginDoc.selectSingleNode(XPATH);

        String subXpath = "validator-class[text() = '" + PACKAGE_NAME + ".MyWorkflowValidator']";

        assertNotNull("valid workflow-validator not found", moduleNode);
        assertNotNull("valid validator-class not found", moduleNode.selectSingleNode(subXpath));
    }

}
