package com.atlassian.plugins.codegen.modules.common.servlet;

import com.atlassian.plugins.codegen.annotations.*;
import com.atlassian.plugins.codegen.modules.AbstractPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;

/**
 * @since version
 */
@RefAppPluginModuleCreator
@JiraPluginModuleCreator
@ConfluencePluginModuleCreator
@BambooPluginModuleCreator
@FeCruPluginModuleCreator
@CrowdPluginModuleCreator
@Dependencies({
        @Dependency(groupId = "javax.servlet", artifactId = "servlet-api", version = "2.4", scope = "provided")
        , @Dependency(groupId = "org.mockito", artifactId = "mockito-all", version = "1.8.5", scope = "test")
        , @Dependency(groupId = "org.apache.httpcomponents", artifactId = "httpclient", version = "4.1.1", scope = "test")
})
public class ServletModuleCreator extends AbstractPluginModuleCreator<ServletProperties> {

    public static final String MODULE_NAME = "Servlet";
    private static final String TEMPLATE_PREFIX = "templates/common/servlet/";

    //stub
    private static final String CLASS_TEMPLATE = TEMPLATE_PREFIX + "Servlet.java.vm";
    private static final String UNIT_TEST_TEMPLATE = TEMPLATE_PREFIX + "ServletTest.java.vm";
    private static final String FUNC_TEST_TEMPLATE = TEMPLATE_PREFIX + "ServletFuncTest.java.vm";

    //examples
    private static final String EXAMPLE_CLASS_TEMPLATE = TEMPLATE_PREFIX + "Example" + CLASS_TEMPLATE;

    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "servlet-plugin.xml.vm";

    @Override
    public void createModule(PluginModuleLocation location, ServletProperties props) throws Exception {
        String packageName = props.getProperty(PluginModuleProperties.PACKAGE);

        String classname = props.getProperty(PluginModuleProperties.CLASSNAME);

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
