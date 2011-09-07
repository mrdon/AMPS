package com.atlassian.plugins.codegen.modules.common.web;

import com.atlassian.plugins.codegen.annotations.*;
import com.atlassian.plugins.codegen.modules.AbstractPluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.common.Label;
import com.atlassian.plugins.codegen.modules.common.Tooltip;

import java.util.Properties;

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
})
public class WebItemModuleCreator extends AbstractPluginModuleCreator<WebItemProperties> {

    public static final String MODULE_NAME = "Web Item";
    private static final String TEMPLATE_PREFIX = "templates/common/web/webitem/";

    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "web-item-plugin.xml.vtl";

    @Override
    public void createModule(PluginModuleLocation location, WebItemProperties props) throws Exception {

        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
