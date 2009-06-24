package com.atlassian.maven.plugins.refapp;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;
import com.atlassian.maven.plugins.amps.CreateMojo;

/**
 * Creates a new plugin
 *
 * @extendsPlugin amps
 * @goal create
 * @requiresProject false
 */
public class RefappCreateMojo extends CreateMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return "refapp";
    }
}
