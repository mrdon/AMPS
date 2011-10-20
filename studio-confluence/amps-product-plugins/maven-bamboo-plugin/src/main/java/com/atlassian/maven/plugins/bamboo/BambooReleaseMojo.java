package com.atlassian.maven.plugins.bamboo;

import com.atlassian.maven.plugins.amps.ReleaseMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;

import org.apache.maven.plugin.MojoExecutionException;

public class BambooReleaseMojo extends ReleaseMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return ProductHandlerFactory.BAMBOO;
    }
}
