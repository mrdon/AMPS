package com.atlassian.plugins.codegen.modules.common.component;

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
        @Dependency(groupId = "org.mockito", artifactId = "mockito-all", version = "1.8.5", scope = "test")
})
public class ComponentModuleCreator extends AbstractPluginModuleCreator<ComponentProperties> {

    public static final String MODULE_NAME = "Component";
    private static final String TEMPLATE_PREFIX = "templates/common/component/";

    //stub
    private static final String CLASS_TEMPLATE = TEMPLATE_PREFIX + "Component.java.vtl";
    private static final String INTERFACE_TEMPLATE = TEMPLATE_PREFIX + "ComponentInterface.java.vtl";

    //examples
    private static final String EXAMPLE_CLASS_TEMPLATE = TEMPLATE_PREFIX + "Example" + CLASS_TEMPLATE;

    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "component-plugin.xml.vtl";

    @Override
    public void createModule(PluginModuleLocation location, ComponentProperties props) throws Exception {

        String packageName = props.getPackage();

        String classname = props.getClassname();

        String iClassname = props.getInterfaceClass();
        String iPackage = props.getInterfacePackage();

        if (props.includeExamples()) {
            templateHelper.writeJavaClassFromTemplate(EXAMPLE_CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);
        } else {
            if (props.generateClass()) {
                //main class
                templateHelper.writeJavaClassFromTemplate(CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);

                //unit test
                templateHelper.writeJavaClassFromTemplate(GENERIC_TEST_TEMPLATE, testClassname(classname), location.getTestDirectory(), packageName, props);

                //func test
                templateHelper.writeJavaClassFromTemplate(GENERIC_TEST_TEMPLATE, funcTestClassname(classname), location.getTestDirectory(), funcTestPackageName(packageName), props);
            }

            if(props.generateInterface()) {
                templateHelper.writeJavaClassFromTemplate(INTERFACE_TEMPLATE, iClassname, location.getSourceDirectory(), iPackage, props);
            }

        }


        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
