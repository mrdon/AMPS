package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.util.ArtifactRetriever;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class ProductContext
{
    private String version;
    private String testResourcesVersion;
    private String containerId;
    private String server;
    private int httpPort;
    private String contextPath;
    private String jvmArgs;
    private File log4jProperties;
    private ArtifactRetriever artifactRetriever;

    private List<ProductArtifact> pluginArtifacts = new ArrayList<ProductArtifact>();
    private List<ProductArtifact> libArtifacts = new ArrayList<ProductArtifact>();
    private List<ProductArtifact> bundledArtifacts = new ArrayList<ProductArtifact>();

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

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getTestResourcesVersion()
    {
        return testResourcesVersion;
    }

    public void setTestResourcesVersion(String testResourcesVersion)
    {
        this.testResourcesVersion = testResourcesVersion;
    }

    public List<ProductArtifact> getPluginArtifacts()
    {
        return pluginArtifacts;
    }

    public void setPluginArtifacts(List<ProductArtifact> pluginArtifacts)
    {
        this.pluginArtifacts = pluginArtifacts;
    }

    public List<ProductArtifact> getLibArtifacts()
    {
        return libArtifacts;
    }

    public void setLibArtifacts(List<ProductArtifact> libArtifacts)
    {
        this.libArtifacts = libArtifacts;
    }

    public List<ProductArtifact> getBundledArtifacts()
    {
        return bundledArtifacts;
    }

    public void setBundledArtifacts(List<ProductArtifact> bundledArtifacts)
    {
        this.bundledArtifacts = bundledArtifacts;
    }

    public File getLog4jProperties()
    {
        return log4jProperties;
    }

    public void setLog4jProperties(File log4jProperties)
    {
        this.log4jProperties = log4jProperties;
    }

}
