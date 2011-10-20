package com.atlassian.maven.plugins.bamboo;

import com.atlassian.maven.plugins.amps.IntegrationTestMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.plugin.MojoExecutionException;

public class BambooIntegrationTestMojo extends IntegrationTestMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return ProductHandlerFactory.BAMBOO;
    }
}
