package com.atlassian.maven.plugins.jira;

import com.atlassian.maven.plugins.amps.cli.CliMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @extendsPlugin amps
 * @goal cli
 */
public class JiraCliMojo extends CliMojo
{
    @Override
    protected String getProductId() throws MojoExecutionException
    {
        return "jira";
    }
}
