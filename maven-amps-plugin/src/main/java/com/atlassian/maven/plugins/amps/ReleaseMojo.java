package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.util.GoogleAmpsTracker;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;

@MojoGoal("release")
public class ReleaseMojo extends AbstractProductHandlerMojo
{
    @Override
    protected void doExecute() throws MojoExecutionException, MojoFailureException
    {
        String trackingLabel = getPluginInformation().getId() + ":" + getPluginInformation().getVersion();
        getGoogleTracker().track(GoogleAmpsTracker.RELEASE,trackingLabel);

        getMavenGoals().release();
    }
}
