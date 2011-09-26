package com.atlassian.plugins.codegen.modules.common.web;

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

@Dependencies({
        @Dependency(groupId = "org.mockito", artifactId = "mockito-all", version = "1.8.5", scope = "test")
})
public class WebResourceModuleCreator extends AbstractPluginModuleCreator<WebResourceProperties>
{

    public static final String MODULE_NAME = "Web Resource";
    private static final String TEMPLATE_PREFIX = "templates/common/web/webresource/";
    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "web-resource-plugin.xml.vtl";

    @Override
    public void createModule(PluginModuleLocation location, WebResourceProperties props) throws Exception
    {
        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getModuleName()
    {
        return MODULE_NAME;
    }
}
