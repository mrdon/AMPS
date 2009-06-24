package com.atlassian.maven.plugins.confluence;

import com.atlassian.maven.plugins.amps.IntegrationTestMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @extendsPlugin amps
 * @goal integration-test
 */
public class ConfluenceIntegrationTestMojo extends IntegrationTestMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return "confluence";
    }
}
