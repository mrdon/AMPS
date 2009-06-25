package com.atlassian.maven.plugins.refapp.pdk;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import com.atlassian.maven.plugins.amps.pdk.UninstallMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;

/**
 * @extendsPlugin amps
 * @goal uninstall
 */
public class RefappUninstallMojo extends UninstallMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return ProductHandlerFactory.REFAPP;
    }
}
