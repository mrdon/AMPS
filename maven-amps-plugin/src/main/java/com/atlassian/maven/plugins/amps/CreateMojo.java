package com.atlassian.maven.plugins.amps;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;

/**
 * Creates a new plugin
 *
 * @goal create
 * @requiresProject false
 */
public class CreateMojo extends AbstractAmpsMojo
{
    /**
     * The Maven Project Object
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;
    /**
     * The Maven Session Object
     *
     * @parameter expression="${session}"
     * @required
     * @readonly
     */
    protected MavenSession session;
    /**
     * The Maven PluginManager Object
     *
     * @component
     * @required
     */
    protected PluginManager pluginManager;


    public void execute() throws MojoExecutionException, MojoFailureException {
        com.atlassian.maven.plugins.amps.MavenGoals goals = new MavenGoals(new MavenContext(project, session, pluginManager, getLog()));
        goals.createPlugin(getProductId());
    }
}
