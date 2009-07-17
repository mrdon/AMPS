package com.atlassian.maven.plugins.amps;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;

/**
 * Copies bundled dependencies into META-INF/lib
 */
@MojoGoal("copy-bundled-dependencies")
public class CopyBundledDependenciesMojo extends AbstractAmpsMojo
{
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        getMavenGoals().copyBundledDependencies();
    }
}
