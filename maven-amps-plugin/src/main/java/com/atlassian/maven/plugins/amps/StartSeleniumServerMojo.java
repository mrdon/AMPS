package com.atlassian.maven.plugins.amps;

import org.apache.maven.plugin.MojoExecutionException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;

import java.util.Map;

/**
 * Starts the selenium server
 */
@MojoGoal ("start-selenium-server")
public class StartSeleniumServerMojo extends AbstractSeleniumMojo
{

    @Override
    void doExecute(final Map selenium) throws MojoExecutionException
    {
        getMavenGoals().startSeleniumServer(selenium);
    }
}
