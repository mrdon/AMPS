package com.atlassian.maven.plugins.fecru;

import com.atlassian.maven.plugins.amps.DebugMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.plugin.MojoExecutionException;

public class FeCruDebugMojo extends DebugMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return ProductHandlerFactory.FECRU;
    }
}
