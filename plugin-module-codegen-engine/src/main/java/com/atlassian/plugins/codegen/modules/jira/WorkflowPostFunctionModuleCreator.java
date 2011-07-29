package com.atlassian.plugins.codegen.modules.jira;

import com.atlassian.plugins.codegen.annotations.Dependencies;
import com.atlassian.plugins.codegen.annotations.Dependency;
import com.atlassian.plugins.codegen.annotations.JiraPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.AbstractPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;

/**
 * Author: jdoklovic
 */
@JiraPluginModuleCreator
@Dependencies({
        @Dependency(groupId = "org.mockito", artifactId = "mockito-all", version = "1.8.5", scope = "test")
})
public class WorkflowPostFunctionModuleCreator extends AbstractPluginModuleCreator<WorkflowPostFunctionProperties> {
    public static final String MODULE_NAME = "Workflow Post Function";
    private static final String TEMPLATE_PREFIX = "templates/jira/workflow/function/";

    private static final String FUNCTION_TEMPLATE = TEMPLATE_PREFIX + "PostFunction.java.vtl";
    private static final String FACTORY_TEMPLATE = TEMPLATE_PREFIX + "PostFunctionFactory.java.vtl";
    private static final String UNIT_TEST_TEMPLATE = TEMPLATE_PREFIX + "PostFunctionTest.java.vtl";
    private static final String VIEW_TEMPLATE = TEMPLATE_PREFIX + "post-function.vm.vtl";
    private static final String INPUT_TEMPLATE = TEMPLATE_PREFIX + "post-function-input.vm.vtl";
    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "post-function-plugin.xml.vtl";

    @Override
    public void createModule(PluginModuleLocation location, WorkflowPostFunctionProperties props) throws Exception {
        String moduleKey = props.getModuleKey();
        String viewFileName = moduleKey + ".vm";
        String inputFileName = moduleKey + "-input.vm";
        String packageName = props.getPackage();
        String functionClass = props.getClassname();
        String factoryClass = props.getFactoryName();


        templateHelper.writeJavaClassFromTemplate(FUNCTION_TEMPLATE, functionClass, location.getSourceDirectory(), packageName, props);
        templateHelper.writeJavaClassFromTemplate(FACTORY_TEMPLATE, factoryClass, location.getSourceDirectory(), packageName, props);

        //unit test
        templateHelper.writeJavaClassFromTemplate(UNIT_TEST_TEMPLATE, testClassname(functionClass), location.getTestDirectory(), packageName, props);
        templateHelper.writeFileFromTemplate(VIEW_TEMPLATE, viewFileName, location.getTemplateDirectory(), props);
        templateHelper.writeFileFromTemplate(INPUT_TEMPLATE, inputFileName, location.getTemplateDirectory(), props);

        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);

    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
