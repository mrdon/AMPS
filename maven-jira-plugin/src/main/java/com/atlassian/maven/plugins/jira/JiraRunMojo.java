package com.atlassian.maven.plugins.jira;

import com.atlassian.maven.plugins.amps.RunMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @extendsPlugin amps
 * @goal run
 * @execute phase="package"
 */
public class JiraRunMojo extends RunMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return "jira";
    }
}
