package com.atlassian.maven.plugins.confluence;

import com.atlassian.maven.plugins.amps.DebugMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @extendsPlugin amps
 * @goal debug
 * @execute phase="package"
 */
public class ConfluenceDebugMojo extends DebugMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return ProductHandlerFactory.CONFLUENCE;
    }
}
