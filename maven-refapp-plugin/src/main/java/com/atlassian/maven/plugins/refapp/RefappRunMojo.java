package com.atlassian.maven.plugins.refapp;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import com.atlassian.maven.plugins.amps.RunMojo;

/**
 * Run the webapp
 *
 * @extendsPlugin amps
 * @requiresDependencyResolution run
 * @goal run
 * @execute phase="package"
 */
public class RefappRunMojo
        extends RunMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return "refapp";
    }
}
