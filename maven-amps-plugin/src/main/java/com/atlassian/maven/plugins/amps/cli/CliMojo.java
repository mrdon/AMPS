package com.atlassian.maven.plugins.amps.cli;

import com.atlassian.maven.plugins.amps.AbstractProductAwareMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

@MojoGoal("cli")
public class CliMojo extends AbstractProductAwareMojo
{
    @MojoParameter(expression = "${cli.port}", defaultValue = "4330")
    private int cliPort;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        getMavenGoals().startCli(getPluginInformation(), cliPort);
    }
}
