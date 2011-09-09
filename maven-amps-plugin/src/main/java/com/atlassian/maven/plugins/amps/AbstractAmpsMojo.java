package com.atlassian.maven.plugins.amps;

import java.util.List;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.jfrog.maven.annomojo.annotations.MojoComponent;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

public abstract class AbstractAmpsMojo extends AbstractMojo
{
    /**
     * The Maven Project Object
     */
    @MojoParameter (expression = "${project}", required = true, readonly = true)
    private MavenProject project;

    /**
     * The list of modules being built, the reactor
     */
    @MojoParameter (expression = "${reactorProjects}", required = true, readonly = true)
    private List<MavenProject> reactor;

    /**
     * The Maven Session Object
     */
    @MojoParameter (expression = "${session}", required = true, readonly = true)
    private MavenSession session;

    /**
     * The Maven PluginManager Object
     */
    @MojoComponent
    private PluginManager pluginManager;

    /**
     * The current Maven plugin artifact id
     */
    @MojoParameter (expression = "${plugin.artifactId}", required = true, readonly = true)
    private String pluginArtifactId;

    /**
     * The current Maven plugin version
     */
    @MojoParameter (expression = "${plugin.version}", required = true, readonly = true)
    private String pluginVersion;

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
            try
            {
                Object buildPluginManager = (BuildPluginManager) session.lookup("org.apache.maven.plugin.BuildPluginManager");

                /* Maven 3 */
                mavenContext = new MavenContext(project, reactor, session, (BuildPluginManager) buildPluginManager, getLog());
            }
            catch (ComponentLookupException e)
            {
                /* Maven 2 */
                mavenContext = new MavenContext(project, reactor, session, pluginManager, getLog());
            }
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

    protected PluginInformation getPluginInformation()
    {
        final String productId = pluginArtifactId.replaceAll("maven-(.*)-plugin", "$1");
        return new PluginInformation(productId, pluginVersion);
    }
}
