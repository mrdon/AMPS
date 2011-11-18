package com.atlassian.maven.plugins.fecru;

import com.atlassian.maven.plugins.amps.RunMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.plugin.MojoExecutionException;

public class FeCruRunMojo extends RunMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return ProductHandlerFactory.FECRU;
    }
}
