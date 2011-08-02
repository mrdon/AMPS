package com.atlassian.plugins.codegen.modules.jira;

import com.atlassian.plugins.codegen.annotations.Dependencies;
import com.atlassian.plugins.codegen.annotations.Dependency;
import com.atlassian.plugins.codegen.annotations.JiraPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.AbstractPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;

import java.io.File;

/**
 * @since version
 */
@JiraPluginModuleCreator
@Dependencies({
        @Dependency(groupId = "org.mockito", artifactId = "mockito-all", version = "1.8.5", scope = "test")
})
public class IssueTabPanelModuleCreator extends AbstractPluginModuleCreator<TabPanelProperties> {

    public static final String MODULE_NAME = "Issue Tab Panel";
    private static final String TEMPLATE_PREFIX = "templates/jira/tabpanel/issue/";

    //stub
    private static final String CLASS_TEMPLATE = TEMPLATE_PREFIX + "IssueTabPanel.java.vtl";
    private static final String UNIT_TEST_TEMPLATE = "templates/generic/GenericTest.java.vtl";
    private static final String FUNC_TEST_TEMPLATE = TEMPLATE_PREFIX + "IssueTabPanelFuncTest.java.vtl";
    private static final String VIEW_TEMPLATE = TEMPLATE_PREFIX + "issue-tab-panel.vm.vtl";

    //examples
    private static final String EXAMPLE_CLASS_TEMPLATE = TEMPLATE_PREFIX + "Example" + CLASS_TEMPLATE;

    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "issue-tab-panel-plugin.xml.vtl";

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
            //main class
            templateHelper.writeJavaClassFromTemplate(CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);

            //unit test
            templateHelper.writeJavaClassFromTemplate(UNIT_TEST_TEMPLATE, testClassname(classname), location.getTestDirectory(), packageName, props);

            templateHelper.writeFileFromTemplate(VIEW_TEMPLATE, viewFileName, templatesDir, props);
        }


        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
