package com.atlassian.maven.plugins.amps;

import org.apache.maven.plugin.MojoExecutionException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;

import java.util.Map;

/**
 * Stops the selenium server
 */
@MojoGoal ("stop-selenium-server")
public class StopSeleniumServerMojo extends AbstractSeleniumMojo
{

    @Override
    void doExecute(final Map selenium) throws MojoExecutionException
    {
        getMavenGoals().stopSeleniumServer(selenium);
    }
}
