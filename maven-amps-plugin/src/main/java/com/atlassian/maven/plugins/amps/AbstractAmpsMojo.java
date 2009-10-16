package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;
import org.jfrog.maven.annomojo.annotations.MojoComponent;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

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
     * the maven context
     */
    private MavenContext mavenContext;

    /**
     * the maven goals
     */
    private MavenGoals mavenGoals;

    /**
     * Information about the currently used plugin
     */
    private PluginInformation pluginInformation;

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

    protected PluginInformation getPluginInformation()
    {
        if (pluginInformation != null)
        {
            return pluginInformation;
        }

        if (project != null)
        {
            for (Plugin plugin : (List<Plugin>) project.getBuild().getPlugins())
            {
                if ("com.atlassian.maven.plugins".equals(plugin.getGroupId()))
                {
                    Collection<String> pluginIds = new ArrayList<String>(ProductHandlerFactory.getIds());
                    pluginIds.add("amps");
                    for (String pluginId : pluginIds)
                    {
                        if (("maven-" + pluginId + "-plugin").equals(plugin.getArtifactId()))
                        {
                            pluginInformation = new PluginInformation(pluginId, plugin.getVersion() != null ? plugin.getVersion() : "RELEASE");
                            return pluginInformation;
                        }
                    }

                }
            }
        }
        return new PluginInformation(null, "RELEASE");
    }
}
