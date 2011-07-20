package com.atlassian.plugins.codegen.modules.common.web;

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
@CrowdPluginModuleCreator
@Dependencies({
        @Dependency(groupId = "org.mockito", artifactId = "mockito-all", version = "1.8.5", scope = "test")
})
public class WebSectionModuleCreator extends AbstractPluginModuleCreator<WebSectionProperties> {

    public static final String MODULE_NAME = "Web Section";
    private static final String TEMPLATE_PREFIX = "templates/common/web/websection/";

    private static final String PLUGIN_MODULE_TEMPLATE = TEMPLATE_PREFIX + "web-section-plugin.xml.vtl";

    @Override
    public void createModule(PluginModuleLocation location, WebSectionProperties props) throws Exception {

        addModuleToPluginXml(PLUGIN_MODULE_TEMPLATE, location, props);
    }


    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
