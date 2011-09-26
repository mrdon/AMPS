package com.atlassian.maven.plugins.crowd;

import com.atlassian.maven.plugins.amps.PluginModuleGenerationMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @since 3.5
 */
public class CrowdPluginModuleGenerationMojo extends PluginModuleGenerationMojo{

    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return ProductHandlerFactory.CROWD;
    }
}
