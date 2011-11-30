package com.atlassian.maven.plugins.amps.pdk;

import com.atlassian.maven.plugins.amps.AbstractProductHandlerAwareMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandler;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

/**
 *
 */
public abstract class AbstractPdkMojo extends AbstractProductHandlerAwareMojo
{
    /**
     *
     */
    @MojoParameter(expression = "${atlassian.plugin.key}")
    protected String pluginKey;

    /**
     *
     */
    @MojoParameter(expression = "${project.groupId}")
    protected String groupId;

    /**
     *
     */
    @MojoParameter(expression = "${project.artifactId}")
    protected String artifactId;

    /**
     * HTTP port for the servlet containers
     */
    @MojoParameter(expression = "${http.port}")
    protected int httpPort;

    /**
     * Application context path
     */
    @MojoParameter(expression = "${context.path}")
    protected String contextPath;

    /**
     * Username of user that will install the plugin
     */
    @MojoParameter(expression = "${username}", defaultValue = "admin")
    protected String username;

    /**
     * Password of user that will install the plugin
     */
    @MojoParameter(expression = "${password}", defaultValue = "admin")
    protected String password;

    /**
     * Application server
     */
    @MojoParameter(expression = "${server}", defaultValue = "localhost")
    protected String server;

    protected void ensurePluginKeyExists()
    {
        if (pluginKey == null)
        {
            pluginKey = groupId + "." + artifactId;
        }
    }

    protected int getHttpPort(final ProductHandler handler)
    {
        return httpPort == 0 ? handler.getDefaultHttpPort() : httpPort;
    }

    protected String getContextPath(final ProductHandler handler)
    {
        return contextPath == null ? "/" + handler.getId() : contextPath;
    }
}
