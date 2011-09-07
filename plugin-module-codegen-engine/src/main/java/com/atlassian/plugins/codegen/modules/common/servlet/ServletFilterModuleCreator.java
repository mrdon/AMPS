package com.atlassian.plugins.codegen.modules.common.servlet;

import com.atlassian.plugins.codegen.annotations.*;
import com.atlassian.plugins.codegen.modules.AbstractPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;

/**
 * @since 3.5
 */
@RefAppPluginModuleCreator
@JiraPluginModuleCreator
@ConfluencePluginModuleCreator
@BambooPluginModuleCreator
@Dependencies({
        @Dependency(groupId = "javax.servlet", artifactId = "servlet-api", version = "2.4", scope = "provided")
        , @Dependency(groupId = "org.mockito", artifactId = "mockito-all", version = "1.8.5", scope = "test")
        , @Dependency(groupId = "org.apache.httpcomponents", artifactId = "httpclient", version = "4.1.1", scope = "test")
})
public class ServletFilterModuleCreator extends AbstractPluginModuleCreator<ServletFilterProperties> {

    public static final String MODULE_NAME = "Servlet Filter";
    private static final String TEMPLATE_PREFIX = "templates/common/servlet/filter/";

    //stub
    private static final String CLASS_TEMPLATE = TEMPLATE_PREFIX + "ServletFilter.java.vtl";
    private static final String UNIT_TEST_TEMPLATE = TEMPLATE_PREFIX + "ServletFilterTest.java.vtl";
    private static final String FUNC_TEST_TEMPLATE = TEMPLATE_PREFIX + "ServletFilterFuncTest.java.vtl";

    //examples
    private static final String EXAMPLE_CLASS_TEMPLATE = TEMPLATE_PREFIX + "Example" + CLASS_TEMPLATE;

    private static final String PLUGIN_MODULE_TEMPLATE = "templates/common/servlet/filter/servlet-filter-plugin.xml.vtl";

    @Override
    public void createModule(PluginModuleLocation location, ServletFilterProperties props) throws Exception {
        String packageName = props.getPackage();

        String classname = props.getClassname();

        if (props.includeExamples()) {
            templateHelper.writeJavaClassFromTemplate(EXAMPLE_CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);
        } else {
            //main class
            templateHelper.writeJavaClassFromTemplate(CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);

            //unit test
            templateHelper.writeJavaClassFromTemplate(UNIT_TEST_TEMPLATE, testClassname(classname), location.getTestDirectory(), packageName, props);

            //func test
            templateHelper.writeJavaClassFromTemplate(FUNC_TEST_TEMPLATE, funcTestClassname(classname), location.getTestDirectory(), funcTestPackageName(packageName), props);
        }


        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
