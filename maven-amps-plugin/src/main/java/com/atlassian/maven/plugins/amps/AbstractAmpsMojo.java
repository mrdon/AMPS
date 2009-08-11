package com.atlassian.maven.plugins.amps;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;
import org.jfrog.maven.annomojo.annotations.MojoComponent;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

import java.util.List;

public abstract class AbstractAmpsMojo extends AbstractMojo
{
    /**
     * The Maven Project Object
     */
    @MojoParameter(expression = "${project}", required = true, readonly = true)
    protected MavenProject project;

    /**
     * The list of modules being built, the reactor
     */
    @MojoParameter(expression = "${reactorProjects}", required = true, readonly = true)
    protected List<MavenProject> reactor;

    /**
     * The Maven Session Object
     */
    @MojoParameter(expression = "${session}", required = true, readonly = true)
    protected MavenSession session;

    /**
     * The Maven PluginManager Object
     */
    @MojoComponent
    private PluginManager pluginManager;

    /**
     * the maven context
     */
    private MavenContext mavenContext;

    /**
     * the maven goals
     */
    private MavenGoals mavenGoals;

    protected MavenContext getMavenContext()
    {
        if (mavenContext == null)
        {
            mavenContext = new MavenContext(project, reactor, session, pluginManager, getLog());
        }
        return mavenContext;
    }

    protected MavenGoals getMavenGoals()
    {
        if (mavenGoals == null)
        {
            mavenGoals = new MavenGoals(getMavenContext());
        }
        return mavenGoals;
    }
}
