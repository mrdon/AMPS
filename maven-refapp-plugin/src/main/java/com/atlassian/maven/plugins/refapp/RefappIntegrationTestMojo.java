package com.atlassian.maven.plugins.refapp;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import com.atlassian.maven.plugins.amps.IntegrationTestMojo;

/**
 * Run the integration tests against the webapp
 *
 * @extendsPlugin amps
 * @requiresDependencyResolution integration-test
 * @goal integration-test
 */
public class RefappIntegrationTestMojo
        extends IntegrationTestMojo
{

    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return "refapp";
    }
}