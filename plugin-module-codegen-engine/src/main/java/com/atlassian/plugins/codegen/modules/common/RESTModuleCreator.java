package com.atlassian.plugins.codegen.modules.common;

import com.atlassian.plugins.codegen.annotations.*;
import com.atlassian.plugins.codegen.modules.AbstractPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;

/**
 * @since 3.5
 */
@RefAppPluginModuleCreator
@JiraPluginModuleCreator
@ConfluencePluginModuleCreator
@BambooPluginModuleCreator
@FeCruPluginModuleCreator
@CrowdPluginModuleCreator
@Dependencies({
        @Dependency(groupId = "org.mockito", artifactId = "mockito-all", version = "1.8.5", scope = "test")
        , @Dependency(groupId = "javax.ws.rs", artifactId = "jsr311-api", version = "1.0", scope = "provided")
        , @Dependency(groupId = "javax.xml.bind", artifactId = "jaxb-api", version = "2.1", scope = "provided")
        , @Dependency(groupId = "com.atlassian.plugins.rest", artifactId = "atlassian-rest-common", version = "1.0.2", scope = "provided")
        , @Dependency(groupId = "javax.servlet", artifactId = "servlet-api", version = "2.4", scope = "provided")
        , @Dependency(groupId = "org.apache.wink", artifactId = "wink-client", version = "1.1.3-incubating", scope = "test")
})
public class RESTModuleCreator extends AbstractPluginModuleCreator<RESTProperties>
{

    public static final String MODULE_NAME = "REST Plugin Module";
    private static final String TEMPLATE_PREFIX = "templates/common/rest/";

    //stub
    private static final String CLASS_TEMPLATE = TEMPLATE_PREFIX + "REST.java.vtl";
    private static final String MODEL_TEMPLATE = TEMPLATE_PREFIX + "RESTModelObject.java.vtl";
    private static final String UNIT_TEST_TEMPLATE = TEMPLATE_PREFIX + "RESTTest.java.vtl";
    private static final String FUNC_TEST_TEMPLATE = TEMPLATE_PREFIX + "RESTFuncTest.java.vtl";

    //examples
    private static final String EXAMPLE_CLASS_TEMPLATE = TEMPLATE_PREFIX + "Example" + CLASS_TEMPLATE;

    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "rest-plugin.xml.vtl";

    @Override
    public void createModule(PluginModuleLocation location, RESTProperties props) throws Exception
    {
        String packageName = props.getPackage();

        String classname = props.getClassname();
        String modelClassname = classname + "Model";

        if (props.includeExamples())
        {
            templateHelper.writeJavaClassFromTemplate(EXAMPLE_CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);
        } else
        {
            //main class
            templateHelper.writeJavaClassFromTemplate(CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);

            //model class
            templateHelper.writeJavaClassFromTemplate(MODEL_TEMPLATE, modelClassname, location.getSourceDirectory(), packageName, props);

            //unit test
            templateHelper.writeJavaClassFromTemplate(UNIT_TEST_TEMPLATE, testClassname(classname), location.getTestDirectory(), packageName, props);

            //func test
            templateHelper.writeJavaClassFromTemplate(FUNC_TEST_TEMPLATE, funcTestClassname(classname), location.getTestDirectory(), funcTestPackageName(packageName), props);
        }


        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getModuleName()
    {
        return MODULE_NAME;
    }
}
