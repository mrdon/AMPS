package com.atlassian.maven.plugins.jira;

import com.atlassian.maven.plugins.amps.IntegrationTestMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @extendsPlugin amps
 * @goal integration-test
 */
public class JiraIntegrationTestMojo extends IntegrationTestMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return "jira";
    }
}
