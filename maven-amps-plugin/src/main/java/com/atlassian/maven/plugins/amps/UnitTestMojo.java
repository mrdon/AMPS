package com.atlassian.maven.plugins.amps;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @goal unit-test
 */
public class UnitTestMojo extends AbstractAmpsMojo
{
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        getMavenGoals().runUnitTests();
    }
}