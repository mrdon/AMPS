package com.atlassian.maven.plugins.amps.pdk;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;
import com.atlassian.maven.plugins.amps.AbstractAmpsMojo;
import com.atlassian.maven.plugins.amps.ProductHandler;

/**
 *
 */
public abstract class AbstractPdkMojo extends AbstractAmpsMojo
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

    protected void ensurePluginKeyExists() {
        if (pluginKey == null)
        {
            pluginKey = groupId + "." + artifactId;
        }
    }
    

    protected int getHttpPort(ProductHandler handler)
    {
        return httpPort == 0 ? handler.getDefaultHttpPort() : httpPort;
    }

    protected String getContextPath(ProductHandler handler)
    {
        return contextPath == null ? "/" + handler.getId() : "/";
    }
}
