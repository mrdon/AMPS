package com.atlassian.maven.plugins.caviar;

import com.atlassian.maven.plugins.amps.pdk.TestInstallMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @since 3.8
 */
public class CaviarTestInstallMojo extends TestInstallMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return ProductHandlerFactory.CAVIAR;
    }
}
