package com.atlassian.maven.plugins.amps;

import org.apache.maven.project.MavenProject;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.plugin.logging.Log;

public class MavenContext
{
    private final MavenProject project;
    private final MavenSession session;
    private final PluginManager pluginManager;
    private final Log log;

    public MavenContext(final MavenProject project, final MavenSession session, final PluginManager pluginManager, Log log)
    {
        this.project = project;
        this.session = session;
        this.pluginManager = pluginManager;
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

    public Log getLog()
    {
        return log;
    }
}
