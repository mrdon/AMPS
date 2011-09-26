package com.atlassian.maven.plugins.fecru;

import com.atlassian.maven.plugins.amps.PluginModuleGenerationMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @since 3.5
 */
public class FeCruPluginModuleGenerationMojo extends PluginModuleGenerationMojo{

    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return ProductHandlerFactory.FECRU;
    }
}
