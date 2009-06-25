package com.atlassian.maven.plugins.bamboo;

import com.atlassian.maven.plugins.amps.cli.CliMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @extendsPlugin amps
 * @goal cli
 */
public class BambooCliMojo extends CliMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return ProductHandlerFactory.BAMBOO;
    }
}
