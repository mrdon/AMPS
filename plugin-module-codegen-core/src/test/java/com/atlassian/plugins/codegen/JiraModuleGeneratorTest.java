package com.atlassian.plugins.codegen;

import com.atlassian.plugins.codgen.JiraPluginModuleCreatorFactory;
import com.atlassian.plugins.codgen.PluginModuleCreatorFactory;
import com.atlassian.plugins.codgen.modules.common.ServletFilterModuleCreator;
import com.atlassian.plugins.codgen.modules.jira.WorkflowPostFunctionModuleCreator;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Author: jdoklovic
 */
public class JiraModuleGeneratorTest extends AbstractCodegenTestCase {

    @Test
    public void hasCommonModules() {

        PluginModuleCreatorFactory factory = new JiraPluginModuleCreatorFactory();
        Set<String> moduleKeys = factory.getAllModuleCreators().keySet();
        assertTrue(moduleKeys.contains(WorkflowPostFunctionModuleCreator.MODULE_ID));
        assertTrue(moduleKeys.contains(ServletFilterModuleCreator.MODULE_ID));
    }
/*
    public void apiDesign() throws Exception {

        PluginModuleLocation moduleLocation = new PluginModuleLocation.Builder(srcDir)
                .templateDirectory(templateDir)
                .pluginXml(pluginXml)
                .build();

        WorkflowPostFunctionProperties props = new WorkflowPostFunctionProperties("com.atlassian.test.MyPostFunction");
        WorkflowPostFunctionModuleCreator creator = new WorkflowPostFunctionModuleCreator();
        creator.createModule(moduleLocation,props);

        JiraPluginModuleCreatorFactory jiraFactory = new JiraPluginModuleCreatorFactory();
        jiraFactory.getModuleCreator(WorkflowPostFunctionModuleCreator.MODULE_ID);
    }

    @Test
    public void createsMultipleFiles() throws Exception {

        PluginModuleLocation moduleLocation = new PluginModuleLocation.Builder(srcDir)
                .templateDirectory(templateDir)
                .pluginXml(pluginXml)
                .build();

        String functionName = "MyPostFunction";
        String factoryName = functionName + "Factory";
        String inputName = "my-post-function-input.vm";
        String viewName = "my-post-function.vm";

        File packageDir = new File(srcDir,"com/atlassian/test/");

        File javaFunctionClass = new File(packageDir,functionName + ".java");
        File javaFactoryClass = new File(packageDir,factoryName + ".java");
        File inputTemplate = new File(templateDir,inputName);
        File viewTemplate = new File(templateDir,viewName);

        PluginModuleCreatorFactory generator = new JiraPluginModuleCreatorFactory(moduleLocation);
        WorkflowPostFunctionProperties props = new WorkflowPostFunctionProperties();
        props.setFullyQualifiedClassname("com.atlassian.test." + functionName);
        props.setDescription("MY function does something awesome");
        props.setDeletable(true);
        props.setOrderable(true);
        props.setUnique(false);

        generator.generate(JiraPluginModuleCreatorFactory.WORKFLOW_POST_FUNCTION_MODULE,props);

        assertTrue(javaFunctionClass.exists());
        assertTrue(javaFactoryClass.exists());
        assertTrue(inputTemplate.exists());
        assertTrue(viewTemplate.exists());
    }
    */
}
