package com.atlassian.plugins.codegen.modules.jira;

import com.atlassian.plugins.codegen.annotations.*;
import com.atlassian.plugins.codegen.modules.AbstractPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.common.component.ComponentModuleCreator;
import com.atlassian.plugins.codegen.modules.common.component.ComponentProperties;

/**
 * @since version
 */
@JiraPluginModuleCreator
@Dependencies({
        @Dependency(groupId = "org.mockito", artifactId = "mockito-all", version = "1.8.5", scope = "test")
        , @Dependency(groupId = "org.apache.httpcomponents", artifactId = "httpclient", version = "4.1.1", scope = "test")
})
public class RPCModuleCreator extends AbstractPluginModuleCreator<RPCProperties> {

    public static final String MODULE_NAME = "RPC Endpoint Plugin";
    private static final String TEMPLATE_PREFIX = "templates/jira/rpc/";

    //stub
    private static final String CLASS_TEMPLATE = TEMPLATE_PREFIX + "RPCService.java.vtl";
    private static final String INTERFACE_TEMPLATE = TEMPLATE_PREFIX + "RPCServiceInterface.java.vtl";
    private static final String UNIT_TEST_TEMPLATE = "templates/generic/GenericTest.java.vtl";
    private static final String FUNC_TEST_TEMPLATE = TEMPLATE_PREFIX + "RPCServiceFuncTest.java.vtl";

    //examples
    private static final String EXAMPLE_CLASS_TEMPLATE = TEMPLATE_PREFIX + "Example" + CLASS_TEMPLATE;

    private static final String SOAP_PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "soap-rpc-plugin.xml.vtl";
    private static final String XML_PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "xml-rpc-plugin.xml.vtl";

    @Override
    public void createModule(PluginModuleLocation location, RPCProperties props) throws Exception {
        String packageName = props.getPackage();
        String classname = props.getClassname();
        String iClassname = props.getInterfaceClass();
        String iPackage = props.getInterfacePackage();

        if (props.includeExamples()) {
            templateHelper.writeJavaClassFromTemplate(EXAMPLE_CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);
        } else {

            //interface
            templateHelper.writeJavaClassFromTemplate(INTERFACE_TEMPLATE, iClassname, location.getSourceDirectory(), iPackage, props);

            //main class
            templateHelper.writeJavaClassFromTemplate(CLASS_TEMPLATE, classname, location.getSourceDirectory(), packageName, props);

            //unit test
            templateHelper.writeJavaClassFromTemplate(UNIT_TEST_TEMPLATE, testClassname(iClassname), location.getTestDirectory(), packageName, props);

        }

        //add the component element first
        ComponentProperties cProps = new ComponentProperties(props.getFullyQualifiedClassname());
        cProps.setFullyQualifiedInterface(props.getFullyQualifiedInterface());
        cProps.setModuleKey(props.getModuleKey() + "-component");
        cProps.setModuleName(props.getModuleName() + " Component");
        cProps.setDescription("Component For " + props.getModuleName());
        cProps.setDescriptionI18nKey(props.getDescriptionI18nKey() + ".component");
        cProps.setNameI18nKey(props.getNameI18nKey() + ".component");
        cProps.setGenerateClass(false);
        cProps.setGenerateInterface(false);
        cProps.setIncludeExamples(false);
        //

        ComponentModuleCreator componentCreator = new ComponentModuleCreator();
        componentCreator.createModule(location,cProps);

        //now add the rpc element
        if(props.isSoap()) {
            addModuleToPluginXml(SOAP_PLUGIN_MODULE_TEMPLATE, location, props);
        } else {
            addModuleToPluginXml(XML_PLUGIN_MODULE_TEMPLATE, location, props);
        }


    }


    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
