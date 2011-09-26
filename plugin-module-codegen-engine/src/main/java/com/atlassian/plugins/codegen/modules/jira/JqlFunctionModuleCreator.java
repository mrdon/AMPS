package com.atlassian.plugins.codegen.modules.jira;

import com.atlassian.plugins.codegen.annotations.Dependencies;
import com.atlassian.plugins.codegen.annotations.Dependency;
import com.atlassian.plugins.codegen.annotations.JiraPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.AbstractPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;

/**
 * @since 3.5
 */
@JiraPluginModuleCreator
@Dependencies({
        @Dependency(groupId = "org.mockito", artifactId = "mockito-all", version = "1.8.5", scope = "test")
})
public class JqlFunctionModuleCreator extends AbstractPluginModuleCreator<JqlFunctionProperties>
{

    public static final String MODULE_NAME = "JQL Function";
    private static final String TEMPLATE_PREFIX = "templates/jira/jql/";

    //stub
    private static final String CLASS_TEMPLATE = TEMPLATE_PREFIX + "JqlFunction.java.vtl";
    private static final String UNIT_TEST_TEMPLATE = TEMPLATE_PREFIX + "JqlFunctionTest.java.vtl";
    private static final String FUNC_TEST_TEMPLATE = TEMPLATE_PREFIX + "JqlFunctionFuncTest.java.vtl";

    //examples
    private static final String EXAMPLE_CLASS_TEMPLATE = TEMPLATE_PREFIX + "Example" + CLASS_TEMPLATE;

    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "jql-function-plugin.xml.vtl";

    @Override
    public void createModule(PluginModuleLocation location, JqlFunctionProperties props) throws Exception
    {
        String packageName = props.getPackage();

        String classname = props.getClassname();

        if (props.includeExamples())
        {
            templateHelper.writeJavaClassFromTemplate(EXAMPLE_CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);
        } else
        {
            //main class
            templateHelper.writeJavaClassFromTemplate(CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);

            //unit test
            templateHelper.writeJavaClassFromTemplate(UNIT_TEST_TEMPLATE, testClassname(classname), location.getTestDirectory(), packageName, props);

            //func test
            //templateHelper.writeJavaClassFromTemplate(FUNC_TEST_TEMPLATE, funcTestClassname(classname), location.getTestDirectory(), funcTestPackageName(packageName), props);
        }


        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getModuleName()
    {
        return MODULE_NAME;
    }
}
