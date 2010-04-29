package com.atlassian.maven.plugins.amps;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

/**
 * Copies bundled dependencies into META-INF/lib
 */
@MojoGoal("copy-bundled-dependencies")
public class CopyBundledDependenciesMojo extends AbstractAmpsMojo
{
    @MojoParameter(expression = "extractDependencies", defaultValue = "false")
    private Boolean extractDependencies;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if (!extractDependencies)
        {
            getMavenGoals().copyBundledDependencies();
        }
        else
        {
            getMavenGoals().extractBundledDependencies();
        }
    }
}
