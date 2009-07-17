package com.atlassian.maven.plugins.refapp;

import com.atlassian.maven.plugins.amps.IntegrationTestMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Run the integration tests against the webapp
 */
public class RefappIntegrationTestMojo extends IntegrationTestMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return ProductHandlerFactory.REFAPP;
    }
}