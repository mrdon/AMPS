package com.atlassian.maven.plugins.refapp;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;
import com.atlassian.maven.plugins.amps.UnitTestMojo;

/**
 * @extendsPlugin amps
 * @goal unit-test
 */
public class RefappUnitTestMojo extends UnitTestMojo
{

    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return "refapp";
    }

}