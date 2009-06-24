package com.atlassian.maven.plugins.conf;

import com.atlassian.maven.plugins.refapp.MavenContext;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;
import static org.twdata.maven.mojoexecutor.MojoExecutor.artifactId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executeMojo;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executionEnvironment;
import static org.twdata.maven.mojoexecutor.MojoExecutor.goal;
import static org.twdata.maven.mojoexecutor.MojoExecutor.groupId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.name;
import static org.twdata.maven.mojoexecutor.MojoExecutor.plugin;

import java.io.File;

public class ConfluenceMavenGoals
{
    private MavenProject project;
    private MavenSession session;
    private PluginManager pluginManager;

    public ConfluenceMavenGoals(MavenContext ctx) {
        this.project = ctx.getProject();
        this.session = ctx.getSession();
        this.pluginManager = ctx.getPluginManager();
    }

    public File copyConfluenceHome(final File targetDirectory, final String testResourcesVersion) throws MojoExecutionException
    {
        final File testResourcesZip = new File(targetDirectory, "test-resources.zip");
        executeMojo(
                plugin(
                        groupId("org.apache.maven.plugins"),
                        artifactId("maven-dependency-plugin")
                ),
                goal("copy"),
                configuration(
                        element(name("artifactItems"),
                                element(name("artifactItem"),
                                        element(name("groupId"), "com.atlassian.confluence.plugins"),
                                        element(name("artifactId"), "confluence-plugin-test-resources"),
                                        element(name("type"), "zip"),
                                        element(name("version"), testResourcesVersion),
                                        element(name("destFileName"), testResourcesZip.getName()))),
                                        element(name("outputDirectory"), testResourcesZip.getParent())
                ),
                executionEnvironment(project, session, pluginManager)
        );
        return testResourcesZip;
    }
}
