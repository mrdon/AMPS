package com.atlassian.maven.plugins.amps;

/**
 */
public class PluginInformation
{
    private final String productId;
    private final String version;

    public PluginInformation(final String productId, final String version)
    {
        this.productId = productId;
        this.version = version;
    }

    public String getId()
    {
        return productId;
    }

    public String getVersion()
    {
        return version;
    }
}