package com.atlassian.plugins.codgen.modules.jira;

import com.atlassian.plugins.codgen.PluginModuleLocation;
import com.atlassian.plugins.codgen.modules.AbstractPluginModuleCreator;
import com.atlassian.plugins.codgen.modules.BasicModuleProperties;
import com.atlassian.plugins.codgen.util.CodeTemplateHelper;

/**
 * Author: jdoklovic
 */
public class WorkflowPostFunctionModuleCreator extends AbstractPluginModuleCreator {
    public static final String MODULE_ID = "Workflow Post Function";

    private static final String FUNCTION_TEMPLATE = "templates/jira/workflow/function/PostFunction.java.vm";
    private static final String FACTORY_TEMPLATE = "templates/jira/workflow/function/PostFunctionFactory.java.vm";
    private static final String VIEW_TEMPLATE = "templates/jira/workflow/function/post-function.vm.vm";
    private static final String INPUT_TEMPLATE = "templates/jira/workflow/function/post-function-input.vm.vm";
    private static final String PLUGIN_MODULE_TEMPLATE = "templates/jira/workflow/function/post-function-plugin.xml.vm";

    private static final String KEY_PREFIX = "admin.workflow.function.";

    @Override
    public String getId() {
        return MODULE_ID;
    }

    @Override
    public void createModule(PluginModuleLocation location, BasicModuleProperties props) throws Exception {
        String moduleKey = props.getProperty(BasicModuleProperties.MODULE_KEY);
        String moduleName = props.getProperty(BasicModuleProperties.MODULE_NAME);
        String viewFileName = moduleKey + ".vm";
        String inputFileName = moduleKey + "-input.vm";
        String packageName = props.getProperty(BasicModuleProperties.PACKAGE);
        String functionClass = props.getProperty(BasicModuleProperties.CLASSNAME);
        String factoryClass = props.getProperty(WorkflowPostFunctionProperties.FACTORY_NAME);

        if (!props.containsKey(BasicModuleProperties.DESCRIPTION)) {
            props.setProperty(BasicModuleProperties.DESCRIPTION, "The " + moduleName + " Workflow Post Function");
        }

        if (!props.containsKey(BasicModuleProperties.DESCRIPTION_KEY)) {
            props.setProperty(BasicModuleProperties.DESCRIPTION_KEY, KEY_PREFIX + moduleKey);
        }

        if (!props.containsKey(BasicModuleProperties.NAME_KEY)) {
            props.setProperty(BasicModuleProperties.NAME_KEY, KEY_PREFIX + moduleKey + ".name");
        }

        templateHelper.writeJavaClassFromTemplate(FUNCTION_TEMPLATE, functionClass, location.getSourceDirectory(), packageName, props);
        templateHelper.writeJavaClassFromTemplate(FACTORY_TEMPLATE, factoryClass, location.getSourceDirectory(), packageName, props);
        templateHelper.writeFileFromTemplate(VIEW_TEMPLATE, viewFileName, location.getTemplateDirectory(), props);
        templateHelper.writeFileFromTemplate(INPUT_TEMPLATE, inputFileName, location.getTemplateDirectory(), props);

        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);

    }
}
