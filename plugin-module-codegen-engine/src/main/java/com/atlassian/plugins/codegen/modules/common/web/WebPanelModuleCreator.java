package com.atlassian.plugins.codegen.modules.common.web;

import com.atlassian.plugins.codegen.annotations.*;
import com.atlassian.plugins.codegen.modules.AbstractPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;

/**
 * @since 3.6
 */
@RefAppPluginModuleCreator
@JiraPluginModuleCreator
@ConfluencePluginModuleCreator
@BambooPluginModuleCreator
@Dependencies({
        @Dependency(groupId = "org.mockito", artifactId = "mockito-all", version = "1.8.5", scope = "test")
})
public class WebPanelModuleCreator extends AbstractPluginModuleCreator<WebPanelProperties>
{

    public static final String MODULE_NAME = "Web Panel";
    private static final String TEMPLATE_PREFIX = "templates/common/web/webpanel/";

    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "web-panel-plugin.xml.vtl";

    @Override
    public void createModule(PluginModuleLocation location, WebPanelProperties props) throws Exception
    {

        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getModuleName()
    {
        return MODULE_NAME;
    }
}
