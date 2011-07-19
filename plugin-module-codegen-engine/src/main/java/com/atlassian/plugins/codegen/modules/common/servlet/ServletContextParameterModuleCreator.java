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
})
public class ServletContextParameterModuleCreator extends AbstractPluginModuleCreator<ServletContextParameterProperties> {

    public static final String MODULE_NAME = "Servlet Context Parameter";
    private static final String TEMPLATE_PREFIX = "templates/common/servlet/parameter/";

    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "servlet-context-parameter-plugin.xml.vtl";

    @Override
    public void createModule(PluginModuleLocation location, ServletContextParameterProperties props) throws Exception {

        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
