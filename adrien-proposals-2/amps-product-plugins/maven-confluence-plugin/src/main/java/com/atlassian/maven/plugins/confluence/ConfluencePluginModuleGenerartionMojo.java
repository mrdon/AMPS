package com.atlassian.maven.plugins.confluence;

import com.atlassian.maven.plugins.amps.PluginModuleGenerationMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @since 3.6
 */
public class ConfluencePluginModuleGenerartionMojo extends PluginModuleGenerationMojo{

    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return ProductHandlerFactory.CONFLUENCE;
    }
}
