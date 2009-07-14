package com.atlassian.maven.plugins.amps;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Creates a new plugin
 * @goal create
 * @requiresProject false
 */
public class CreateMojo extends AbstractProductAwareMojo
{
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        getMavenGoals().createPlugin(getProductId());
    }
}
