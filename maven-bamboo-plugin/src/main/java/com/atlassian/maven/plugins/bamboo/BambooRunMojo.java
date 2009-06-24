package com.atlassian.maven.plugins.bamboo;

import com.atlassian.maven.plugins.amps.RunMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @extendsPlugin amps
 * @goal run
 * @execute phase="package"
 */
public class BambooRunMojo extends RunMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return "bamboo";
    }
}
