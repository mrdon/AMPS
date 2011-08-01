package com.atlassian.plugins.codegen.modules.jira;

import com.atlassian.plugins.codegen.annotations.*;
import com.atlassian.plugins.codegen.modules.AbstractPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;

import java.io.File;

/**
 * @since version
 */
@JiraPluginModuleCreator
@Dependencies({
        @Dependency(groupId = "org.mockito", artifactId = "mockito-all", version = "1.8.5", scope = "test")
        , @Dependency(groupId = "org.apache.httpcomponents", artifactId = "httpclient", version = "4.1.1", scope = "test")
})
public class WorkflowValidatorModuleCreator extends AbstractPluginModuleCreator<WorkflowElementProperties> {

    public static final String MODULE_NAME = "Workflow Validator";
    private static final String TEMPLATE_PREFIX = "templates/jira/workflow/validator/";

    //stub
    private static final String FACTORY_TEMPLATE = TEMPLATE_PREFIX + "WorkflowValidatorFactory.java.vtl";
    private static final String CLASS_TEMPLATE = TEMPLATE_PREFIX + "WorkflowValidator.java.vtl";
    private static final String UNIT_TEST_TEMPLATE = TEMPLATE_PREFIX + "WorkflowValidatorTest.java.vtl";
    private static final String FUNC_TEST_TEMPLATE = TEMPLATE_PREFIX + "WorkflowValidatorFuncTest.java.vtl";
    private static final String VIEW_TEMPLATE = TEMPLATE_PREFIX + "workflow-validator.vm.vtl";
    private static final String INPUT_TEMPLATE = TEMPLATE_PREFIX + "workflow-validator-input.vm.vtl";

    //examples
    private static final String EXAMPLE_CLASS_TEMPLATE = TEMPLATE_PREFIX + "Example" + CLASS_TEMPLATE;

    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "workflow-validator-plugin.xml.vtl";

    @Override
    public void createModule(PluginModuleLocation location, WorkflowElementProperties props) throws Exception {
        String moduleKey = props.getModuleKey();
        String viewFileName = moduleKey + ".vm";
        String inputFileName = moduleKey + "-input.vm";
        String packageName = props.getPackage();
        String functionClass = props.getClassname();
        String factoryClass = props.getFactoryName();

        File templatesDir = new File(location.getTemplateDirectory(),"validators");

        if (props.includeExamples()) {
        } else {
            templateHelper.writeJavaClassFromTemplate(CLASS_TEMPLATE, functionClass, location.getSourceDirectory(), packageName, props);
        templateHelper.writeJavaClassFromTemplate(FACTORY_TEMPLATE, factoryClass, location.getSourceDirectory(), packageName, props);

        //unit test
        templateHelper.writeJavaClassFromTemplate(UNIT_TEST_TEMPLATE, testClassname(functionClass), location.getTestDirectory(), packageName, props);
        templateHelper.writeFileFromTemplate(VIEW_TEMPLATE, viewFileName, templatesDir, props);
        templateHelper.writeFileFromTemplate(INPUT_TEMPLATE, inputFileName, templatesDir, props);
        }


        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
