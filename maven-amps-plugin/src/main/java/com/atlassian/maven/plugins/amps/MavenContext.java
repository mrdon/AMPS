package com.atlassian.maven.plugins.amps;

import java.util.List;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

public class MavenContext
{
    private final MavenProject project;
    private final List<MavenProject> reactor;
    private final MavenSession session;

    /* Maven 2 */
    private final PluginManager pluginManager;

    /* Maven 3 */
    private final BuildPluginManager buildPluginManager;

    private final Log log;

    public MavenContext(final MavenProject project, List<MavenProject> reactor, final MavenSession session, final PluginManager pluginManager, Log log)
    {
        this(project, reactor, session, pluginManager, null, log);
    }

    public MavenContext(final MavenProject project, List<MavenProject> reactor, final MavenSession session,
            BuildPluginManager buildPluginManager,
            Log log)
    {
        this(project, reactor, session, null, buildPluginManager, log);
    }

    private MavenContext(final MavenProject project, List<MavenProject> reactor, final MavenSession session,
            final PluginManager pluginManager,
            BuildPluginManager buildPluginManager,
            Log log)
    {
        this.project = project;
        this.reactor = reactor;
        this.session = session;

        this.pluginManager = pluginManager;

        this.buildPluginManager = buildPluginManager;

        this.log = log;
    }

    public MavenProject getProject()
    {
        return project;
    }

    public MavenSession getSession()
    {
        return session;
    }

    public PluginManager getPluginManager()
    {
        return pluginManager;
    }

    public BuildPluginManager getBuildPluginManager()
    {
        return buildPluginManager;
    }

    public Log getLog()
    {
        return log;
    }

    public List<MavenProject> getReactor()
    {
        return reactor;
    }
}
