package com.atlassian.maven.plugins.amps;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoRequiresDependencyResolution;

/**
 * Generates the files needed by Jersey at runtime to provide an extended WADL including docs
 * @since 3.6.1
 */
@MojoGoal("generate-rest-docs")
@MojoRequiresDependencyResolution("test")
public class GenerateRestDocsMojo extends AbstractAmpsMojo
{

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        getMavenGoals().generateRestDocs();
    }
}
