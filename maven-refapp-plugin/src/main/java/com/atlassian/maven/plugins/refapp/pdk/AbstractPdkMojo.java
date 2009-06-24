package com.atlassian.maven.plugins.refapp.pdk;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;
import com.atlassian.maven.plugins.refapp.MavenGoals;
import com.atlassian.maven.plugins.refapp.MavenContext;
import com.atlassian.maven.plugins.refapp.WebappHandler;
import com.atlassian.maven.plugins.refapp.RefappWebappHandler;

/**
 *
 */
public abstract class AbstractPdkMojo extends AbstractMojo {
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
    /**
     * @parameter expression="${atlassian.plugin.key}"
     */
    protected String pluginKey;
    /**
     * @parameter expression="${project.groupId}"
     */
    protected String groupId;
    /**
     * @parameter expression="${project.artifactId}"
     */
    protected String artifactId;

    /**
     * HTTP port for the servlet containers
     *
     * @parameter expression="${http.port}"
     */
    private int httpPort;

    /**
     * Application context path
     *
     * @parameter expression="${context.path}"
     */
    protected String contextPath;

    /**
     * Application server
     *
     * @parameter expression="${server}" default-value="localhost"
     */
    protected String server;

    public AbstractPdkMojo()
    {
        if (contextPath == null)
        {
            contextPath = "/" + getWebappHandler().getId();
        }
    }

    protected void ensurePluginKeyExists() {
        if (pluginKey == null)
        {
            pluginKey = groupId + "." + artifactId;
        }
    }
    
    protected WebappHandler getWebappHandler()
    {
         return new RefappWebappHandler();
    }

    protected int getHttpPort()
    {
        return httpPort == 0 ? getWebappHandler().getDefaultHttpPort() : httpPort;
    }
}
