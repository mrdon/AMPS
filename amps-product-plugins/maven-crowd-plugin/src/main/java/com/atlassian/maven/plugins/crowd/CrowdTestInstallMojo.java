package com.atlassian.maven.plugins.crowd;

import com.atlassian.maven.plugins.amps.pdk.TestInstallMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.plugin.MojoExecutionException;

public class CrowdTestInstallMojo extends TestInstallMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return ProductHandlerFactory.CROWD;
    }
}
