package com.atlassian.maven.plugins.amps;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;
import org.jfrog.maven.annomojo.annotations.MojoRequiresDependencyResolution;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@MojoGoal("unit-test")
@MojoRequiresDependencyResolution("test")
public class UnitTestMojo extends AbstractAmpsMojo
{
    /**
     * System Properties to pass to surefire using a more familiar syntax.
     *
     * @since 3.3
     */
    @MojoParameter
    protected Map<String, Object> systemPropertyVariables = new HashMap<String, Object>();

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        getMavenGoals().runUnitTests(systemPropertyVariables);
    }
}