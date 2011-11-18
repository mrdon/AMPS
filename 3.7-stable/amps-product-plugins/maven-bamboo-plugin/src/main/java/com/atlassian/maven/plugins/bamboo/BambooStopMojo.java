package com.atlassian.maven.plugins.bamboo;

import org.apache.maven.plugin.MojoExecutionException;

import com.atlassian.maven.plugins.amps.StopMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;

public class BambooStopMojo extends StopMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return ProductHandlerFactory.BAMBOO;
    }
}
