package com.atlassian.plugins.codegen.modules.common;

import com.atlassian.plugins.codegen.modules.AbstractPluginModuleProperties;

/**
 * @since 3.6
 */
public class DownloadablePluginResourceProperties extends AbstractPluginModuleProperties
{
    private boolean includeExamples;

    public static final String RESOURCE = "RESOURCE";

    public DownloadablePluginResourceProperties(Resource resource)
    {
        super();
        includeExamples = false;
        put(RESOURCE, resource);
    }

    public Resource getResource()
    {
        return (Resource) get(RESOURCE);
    }
}
