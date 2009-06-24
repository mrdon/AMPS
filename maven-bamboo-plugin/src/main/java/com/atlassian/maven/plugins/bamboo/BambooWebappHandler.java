package com.atlassian.maven.plugins.bamboo;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import com.atlassian.maven.plugins.amps.AbstractWebappMojo;
import com.atlassian.maven.plugins.amps.WebappArtifact;
import com.atlassian.maven.plugins.amps.WebappHandler;
import com.atlassian.maven.plugins.amps.util.ConfigFileUtils;

public class BambooWebappHandler implements WebappHandler
{
    public String getId()
    {
        return "bamboo";
    }

    public WebappArtifact getArtifact()
    {
        return new WebappArtifact("com.atlassian.bamboo", "atlassian-bamboo-web-app", "RELEASE");
    }

    public WebappArtifact getTestResourcesArtifact()
    {
        return new WebappArtifact("com.atlassian.bamboo.plugins", "bamboo-plugin-test-resources", "LATEST");
    }

    public int getDefaultHttpPort()
    {
        return 5990;
    }

    public Map<String, String> getSystemProperties(final MavenProject project)
    {
        return Collections.singletonMap("bamboo.home", getHomeDirectory(project).getPath());
    }

    public Collection<WebappArtifact> getSalArtifacts(final String salVersion)
    {
        return Arrays.asList(
                new WebappArtifact("com.atlassian.sal", "sal-api", salVersion),
                new WebappArtifact("com.atlassian.sal", "sal-bamboo-plugin", salVersion));
    }

    public File getPluginsDirectory(final String webappDir, final File homeDir)
    {
        // indicates plugins should be bundled
        return null;
    }

    public List<WebappArtifact> getExtraContainerDependencies()
    {
        return Collections.emptyList();
    }

    public String getBundledPluginPath()
    {
        return "WEB-INF/classes/atlassian-bundled-plugins.zip";
    }

    public File getHomeDirectory(final MavenProject project)
    {
        return new File(new File(project.getBuild().getDirectory(), getId()), "bamboo-home");
    }

    public void processHomeDirectory(final MavenProject project, final File homeDir, final AbstractWebappMojo webappMojo) throws MojoExecutionException
    {
        ConfigFileUtils.replace(new File(homeDir, "bamboo.cfg.xml"), "@project-dir@", homeDir.getParent());
        ConfigFileUtils.replace(new File(homeDir, "bamboo.cfg.xml"), "${bambooHome}", homeDir.getAbsolutePath());
        ConfigFileUtils.replace(new File(homeDir, "/xml-data/configuration/administration.xml"),
                "http://192.168.15.145:8085", "http://" + webappMojo.getServer() + ":" + webappMojo.getHttpPort() + "/" + webappMojo.getContextPath().replaceAll("^/|/$", ""));
    }

    public List<WebappArtifact> getDefaultPlugins()
    {
        return Collections.emptyList();
    }

    public List<WebappArtifact> getDefaultLibPlugins()
    {
        return Collections.emptyList();
    }

    public List<WebappArtifact> getDefaultBundledPlugins()
    {
        return Collections.emptyList();
    }

}
