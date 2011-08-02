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
public class ComponentTabPanelModuleCreator extends AbstractPluginModuleCreator<TabPanelProperties> {

    public static final String GENERIC_CLASS = "GenericTabPanel";
    public static final String GENERIC_PACKAGE = "com.atlassian.jira.plugin.componentpanel.impl";
    public static final String FQ_GENERIC_CLASS = GENERIC_PACKAGE + "." + GENERIC_CLASS;

    public static final String MODULE_NAME = "Component Tab Panel";
    private static final String TEMPLATE_PREFIX = "templates/jira/tabpanel/component/";

    //stub
    private static final String CLASS_TEMPLATE = TEMPLATE_PREFIX + "ComponentTabPanel.java.vtl";
    private static final String UNIT_TEST_TEMPLATE = "templates/generic/GenericTest.java.vtl";
    private static final String FUNC_TEST_TEMPLATE = TEMPLATE_PREFIX + "ComponentTabPanelFuncTest.java.vtl";
    private static final String VIEW_TEMPLATE = TEMPLATE_PREFIX + "component-tab-panel.vm.vtl";

    //examples
    private static final String EXAMPLE_CLASS_TEMPLATE = TEMPLATE_PREFIX + "Example" + CLASS_TEMPLATE;

    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "component-tab-panel-plugin.xml.vtl";

    @Override
    public void createModule(PluginModuleLocation location, TabPanelProperties props) throws Exception {
        String moduleKey = props.getModuleKey();
        String packageName = props.getPackage();
        String classname = props.getClassname();
        String viewFileName = moduleKey + ".vm";
        File templatesDir = new File(location.getTemplateDirectory(), "tabpanels");

        if (props.includeExamples()) {
            templateHelper.writeJavaClassFromTemplate(EXAMPLE_CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);
        } else {

            if(props.isUseCustomClass()) {
                //main class
                templateHelper.writeJavaClassFromTemplate(CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);

                //unit test
                templateHelper.writeJavaClassFromTemplate(UNIT_TEST_TEMPLATE, testClassname(classname), location.getTestDirectory(), packageName, props);
            }

            templateHelper.writeFileFromTemplate(VIEW_TEMPLATE, viewFileName, templatesDir, props);
        }


        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
