package com.atlassian.maven.plugins.refapp;

import com.atlassian.maven.plugins.refapp.util.ArtifactRetriever;

import java.io.File;

public class WebappContext
{
    private File webappWar;
    private String containerId;
    private String server;
    private int httpPort;
    private String contextPath;
    private String jvmArgs;
    private ArtifactRetriever artifactRetriever;

    public File getWebappWar()
    {
        return webappWar;
    }

    public void setWebappWar(File webappWar)
    {
        this.webappWar = webappWar;
    }

    public String getContainerId()
    {
        return containerId;
    }

    public void setContainerId(String containerId)
    {
        this.containerId = containerId;
    }

    public String getServer()
    {
        return server;
    }

    public void setServer(String server)
    {
        this.server = server;
    }

    public int getHttpPort()
    {
        return httpPort;
    }

    public void setHttpPort(int httpPort)
    {
        this.httpPort = httpPort;
    }

    public String getContextPath()
    {
        return contextPath;
    }

    public void setContextPath(String contextPath)
    {
        this.contextPath = contextPath;
    }

    public String getJvmArgs()
    {
        return jvmArgs;
    }

    public void setJvmArgs(String jvmArgs)
    {
        this.jvmArgs = jvmArgs;
    }

    public ArtifactRetriever getArtifactRetriever()
    {
        return artifactRetriever;
    }

    public void setArtifactRetriever(ArtifactRetriever artifactRetriever)
    {
        this.artifactRetriever = artifactRetriever;
    }
}
