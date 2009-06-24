package com.atlassian.maven.plugins.conf;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import static org.twdata.maven.mojoexecutor.MojoExecutor.goal;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;
import static org.twdata.maven.mojoexecutor.MojoExecutor.name;

import com.atlassian.maven.plugins.refapp.AbstractWebappMojo;
import com.atlassian.maven.plugins.refapp.MavenContext;
import com.atlassian.maven.plugins.refapp.WebappArtifact;
import com.atlassian.maven.plugins.refapp.WebappHandler;
import com.atlassian.maven.plugins.refapp.util.ConfigFileUtils;

public class ConfluenceWebappHandler implements WebappHandler
{
    public String getId()
    {
        return "confluence";
    }

    public WebappArtifact getArtifact()
    {
        return new WebappArtifact("com.atlassian.confluence", "confluence-webapp", "RELEASE");
    }

    public WebappArtifact getTestResourcesArtifact()
    {
        return new WebappArtifact("com.atlassian.confluence.plugins", "confluence-plugin-test-resources", "LATEST");
    }

    public int getDefaultHttpPort()
    {
        return 1990;
    }

    public Map<String, String> getSystemProperties(final MavenProject project)
    {
        return Collections.singletonMap("confluence.home", getHomeDirectory(project).getPath());
    }

    public Collection<WebappArtifact> getSalArtifacts(final String salVersion)
    {
        return Arrays.asList(
                new WebappArtifact("com.atlassian.sal", "sal-api", salVersion),
                new WebappArtifact("com.atlassian.sal", "sal-confluence-plugin", salVersion));
    }

    public File getPluginsDirectory(final String webappDir, File homeDir)
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
        return "WEB-INF/classes/com/atlassian/confluence/setup/atlassian-bundled-plugins.zip";
    }

    public File getHomeDirectory(final MavenProject project)
    {
        return new File(new File(project.getBuild().getDirectory(), getId()), "confluence-home");
    }

    public void processHomeDirectory(MavenProject project, File homeDir, AbstractWebappMojo webappMojo) throws MojoExecutionException
    {
        ConfigFileUtils.replace(new File(homeDir, "confluence.cfg.xml"), "@project-dir@", homeDir.getParent());

        File script = new File(new File(homeDir, "database"), "confluencedb.script");
        if (script.exists())
        {
            ConfigFileUtils.replace(new File(new File(homeDir,"database"), "confluencedb.script"),
                    "<baseUrl>http://localhost:8080</baseUrl>",
                    "<baseUrl>http://"+webappMojo.getServer()+":"+webappMojo.getHttpPort()+"/"+webappMojo.getContextPath().replaceAll("^/|/$", "")+"</baseUrl>");
        }
        else
        {
            ConfigFileUtils.replace(new File(new File(homeDir,"database"), "confluencedb.log"),
                    "<baseUrl>http://localhost:8080</baseUrl>",
                    "<baseUrl>http://"+webappMojo.getServer()+":"+webappMojo.getHttpPort()+"/"+webappMojo.getContextPath().replaceAll("^/|/$", "")+"</baseUrl>");
        }
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
