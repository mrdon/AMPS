package com.atlassian.plugins.codegen.modules.common;

import com.atlassian.plugins.codegen.modules.AbstractPluginModuleProperties;
import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @since version
 */
public class DownloadablePluginResourceProperties extends AbstractPluginModuleProperties {
    private boolean includeExamples;

    public static final String RESOURCE = "RESOURCE";

    public DownloadablePluginResourceProperties(Resource resource) {
        super();
        includeExamples = false;
        put(RESOURCE,resource);
    }

    public Resource getResource() {
        return (Resource)get(RESOURCE);
    }
}
