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
public class ComponentImportModuleCreator extends AbstractPluginModuleCreator {

    public static final String MODULE_NAME = "Component Import";
    private static final String TEMPLATE_PREFIX = "templates/common/component/";

    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "component-import-plugin.xml.vm";

    @Override
    public void createModule(PluginModuleLocation location, PluginModuleProperties props) throws Exception {
        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
