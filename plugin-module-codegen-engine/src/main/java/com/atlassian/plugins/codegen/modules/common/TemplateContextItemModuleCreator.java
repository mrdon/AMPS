package com.atlassian.plugins.codegen.modules.common;

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
public class TemplateContextItemModuleCreator extends AbstractPluginModuleCreator<TemplateContextItemProperties> {

    public static final String MODULE_NAME = "Template Context Item";
    private static final String TEMPLATE_PREFIX = "templates/common/";

    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "template-context-item-plugin.xml.vtl";

    @Override
    public void createModule(PluginModuleLocation location, TemplateContextItemProperties props) throws Exception {
        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
