package com.atlassian.maven.plugins.confluence;

import com.atlassian.maven.plugins.amps.RunMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @extendsPlugin amps
 * @goal run
 * @execute phase="package"
 */
public class ConfluenceRunMojo extends RunMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return "confluence";
    }
}
