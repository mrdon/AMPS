package com.atlassian.maven.plugins.refapp;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import com.atlassian.maven.plugins.amps.DebugMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;

/**
 * Debug the webapp
 *
 * @extendsPlugin amps
 * @requiresDependencyResolution debug
 * @goal debug
 * @execute phase="package"
 */
public class RefappDebugMojo extends DebugMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException {
        return ProductHandlerFactory.REFAPP;
    }
}
