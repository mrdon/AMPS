package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.util.GoogleAmpsTracker;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoRequiresProject;

/**
 * Creates a new plugin
 */
@MojoGoal("create")
@MojoRequiresProject(false)
public class CreateMojo extends AbstractProductAwareMojo
{
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        getGoogleTracker().track(GoogleAmpsTracker.CREATE_PLUGIN);

        getMavenGoals().createPlugin(getProductId());
    }
}
