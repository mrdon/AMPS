package com.atlassian.maven.plugins.refapp;

import com.atlassian.maven.plugins.amps.pdk.InstallMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.plugin.MojoExecutionException;

public class RefappInstallMojo extends InstallMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return ProductHandlerFactory.REFAPP;
    }
}
