package com.atlassian.plugins.codegen.modules.common.web;

import com.atlassian.plugins.codegen.annotations.*;
import com.atlassian.plugins.codegen.modules.AbstractPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;

/**
 * @since 3.5
 */

@Dependencies({
        @Dependency(groupId = "org.mockito", artifactId = "mockito-all", version = "1.8.5", scope = "test")
})
public class SimpleWebModuleCreator extends AbstractPluginModuleCreator<SimpleWebProperties> {

    public static final String MODULE_NAME = "SimpleWebModule";
    private static final String TEMPLATE_PREFIX = "templates/common/web/";

    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "simple-web-module-plugin.xml.vtl";

    @Override
    public void createModule(PluginModuleLocation location, SimpleWebProperties props) throws Exception {
        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
