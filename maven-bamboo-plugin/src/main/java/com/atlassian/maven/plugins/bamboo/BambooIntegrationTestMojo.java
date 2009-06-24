package com.atlassian.maven.plugins.bamboo;

import com.atlassian.maven.plugins.amps.IntegrationTestMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @extendsPlugin amps
 * @goal integration-test
 */
public class BambooIntegrationTestMojo extends IntegrationTestMojo
{
    @Override
    protected String getProductId() throws MojoExecutionException
    {
        return "bamboo";
    }
}
