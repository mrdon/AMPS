package com.atlassian.plugins.codegen.modules.common;

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
@CrowdPluginModuleCreator
@FeCruPluginModuleCreator
@Dependencies({
        @Dependency(groupId = "org.mockito", artifactId = "mockito-all", version = "1.8.5", scope = "test")
})
public class DownloadablePluginResourceModuleCreator extends AbstractPluginModuleCreator<DownloadablePluginResourceProperties>
{

    public static final String MODULE_NAME = "Downloadable Plugin Resource";
    private static final String TEMPLATE_PREFIX = "templates/common/";
    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "resource-plugin.xml.vtl";

    @Override
    public void createModule(PluginModuleLocation location, DownloadablePluginResourceProperties props) throws Exception
    {
        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getModuleName()
    {
        return MODULE_NAME;
    }
}
