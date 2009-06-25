package com.atlassian.maven.plugins.confluence;

import com.atlassian.maven.plugins.amps.pdk.InstallMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @extendsPlugin amps
 * @goal install
 */
public class ConfluenceInstallMojo extends InstallMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return ProductHandlerFactory.CONFLUENCE;
    }
}
