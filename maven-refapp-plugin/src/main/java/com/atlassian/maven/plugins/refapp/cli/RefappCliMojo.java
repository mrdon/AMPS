package com.atlassian.maven.plugins.refapp.cli;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;
import org.apache.maven.execution.MavenSession;
import com.atlassian.maven.plugins.amps.cli.CliMojo;

/**
 * @extendsPlugin amps
 * @goal cli
 */
public class RefappCliMojo extends CliMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return "refapp";
    }
}
