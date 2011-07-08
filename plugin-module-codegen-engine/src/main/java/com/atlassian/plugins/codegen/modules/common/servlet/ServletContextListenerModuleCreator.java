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
@Dependencies({
        @Dependency(groupId = "javax.servlet", artifactId = "servlet-api", version = "2.4", scope = "provided")
        , @Dependency(groupId = "org.mockito", artifactId = "mockito-all", version = "1.8.5", scope = "test")
        , @Dependency(groupId = "org.apache.httpcomponents", artifactId = "httpclient", version = "4.1.1", scope = "test")
})
public class ServletContextListenerModuleCreator extends AbstractPluginModuleCreator {

    public static final String MODULE_NAME = "Servlet Context Listener";
    private static final String TEMPLATE_PREFIX = "templates/common/servlet/listener/";

    //stub
    private static final String CLASS_TEMPLATE = TEMPLATE_PREFIX + "ServletContextListener.java.vm";
    private static final String UNIT_TEST_TEMPLATE = TEMPLATE_PREFIX + "ServletContextListenerTest.java.vm";

    //examples
    private static final String EXAMPLE_CLASS_TEMPLATE = TEMPLATE_PREFIX + "Example" + CLASS_TEMPLATE;

    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "servlet-context-listener-plugin.xml.vm";

    @Override
    public void createModule(PluginModuleLocation location, PluginModuleProperties props) throws Exception {
        String packageName = props.getProperty(PluginModuleProperties.PACKAGE);

        String classname = props.getProperty(PluginModuleProperties.CLASSNAME);

        if (props.includeExamples()) {
            templateHelper.writeJavaClassFromTemplate(EXAMPLE_CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);
        } else {
            //main class
            templateHelper.writeJavaClassFromTemplate(CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);

            //unit test
            templateHelper.writeJavaClassFromTemplate(UNIT_TEST_TEMPLATE, testClassname(classname), location.getTestDirectory(), packageName, props);

            //context listener is too complex to func test without a known servlet

        }


        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
