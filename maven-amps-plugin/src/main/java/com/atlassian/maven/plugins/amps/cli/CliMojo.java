package com.atlassian.maven.plugins.amps.cli;

import com.atlassian.maven.plugins.amps.AbstractProductAwareMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @goal cli
 */
public class CliMojo extends AbstractProductAwareMojo
{
    /**
     * @parameter expression="${cli.port}"
     */
    private int port = 4330;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        getMavenGoals().startCli(getProductId(), port);
    }
}
