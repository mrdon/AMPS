package com.atlassian.maven.plugins.studio;

import com.atlassian.maven.plugins.amps.CreateMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.plugin.MojoExecutionException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;

/**
 * Creates a new plugin
 */
@MojoGoal("create")
public class StudioCreateMojo extends CreateMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return ProductHandlerFactory.STUDIO;
    }
}
