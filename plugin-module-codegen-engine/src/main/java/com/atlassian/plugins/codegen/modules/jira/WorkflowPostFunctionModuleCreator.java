package com.atlassian.plugins.codegen.modules.jira;

import com.atlassian.plugins.codegen.annotations.JiraPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.AbstractPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;

/**
 * Author: jdoklovic
 */
@JiraPluginModuleCreator
public class WorkflowPostFunctionModuleCreator extends AbstractPluginModuleCreator {
    public static final String MODULE_NAME = "Workflow Post Function";

    private static final String FUNCTION_TEMPLATE = "templates/jira/workflow/function/PostFunction.java.vtl";
    private static final String FACTORY_TEMPLATE = "templates/jira/workflow/function/PostFunctionFactory.java.vtl";
    private static final String VIEW_TEMPLATE = "templates/jira/workflow/function/post-function.vm.vtl";
    private static final String INPUT_TEMPLATE = "templates/jira/workflow/function/post-function-input.vm.vtl";
    private static final String PLUGIN_MODULE_TEMPLATE = "templates/jira/workflow/function/post-function-plugin.xml.vtl";

    private static final String KEY_PREFIX = "admin.workflow.function.";

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public void createModule(PluginModuleLocation location, PluginModuleProperties props) throws Exception {
        String moduleKey = props.getProperty(PluginModuleProperties.MODULE_KEY);
        String moduleName = props.getProperty(PluginModuleProperties.MODULE_NAME);
        String viewFileName = moduleKey + ".vm";
        String inputFileName = moduleKey + "-input.vm";
        String packageName = props.getProperty(PluginModuleProperties.PACKAGE);
        String functionClass = props.getProperty(PluginModuleProperties.CLASSNAME);
        String factoryClass = props.getProperty(WorkflowPostFunctionProperties.FACTORY_NAME);

        if (!props.containsKey(PluginModuleProperties.DESCRIPTION)) {
            props.setProperty(PluginModuleProperties.DESCRIPTION, "The " + moduleName + " Workflow Post Function");
        }

        if (!props.containsKey(PluginModuleProperties.DESCRIPTION_I18N_KEY)) {
            props.setProperty(PluginModuleProperties.DESCRIPTION_I18N_KEY, KEY_PREFIX + moduleKey);
        }

        if (!props.containsKey(PluginModuleProperties.NAME_I18N_KEY)) {
            props.setProperty(PluginModuleProperties.NAME_I18N_KEY, KEY_PREFIX + moduleKey + ".name");
        }

        templateHelper.writeJavaClassFromTemplate(FUNCTION_TEMPLATE, functionClass, location.getSourceDirectory(), packageName, props);
        templateHelper.writeJavaClassFromTemplate(FACTORY_TEMPLATE, factoryClass, location.getSourceDirectory(), packageName, props);
        templateHelper.writeFileFromTemplate(VIEW_TEMPLATE, viewFileName, location.getTemplateDirectory(), props);
        templateHelper.writeFileFromTemplate(INPUT_TEMPLATE, inputFileName, location.getTemplateDirectory(), props);

        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);

    }
}
