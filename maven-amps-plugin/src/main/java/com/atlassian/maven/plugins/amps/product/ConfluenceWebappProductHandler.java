package com.atlassian.maven.plugins.amps.product;

import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.ProductContext;
import com.atlassian.maven.plugins.amps.util.ConfigFileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ConfluenceWebappProductHandler extends AbstractWebappProductHandler
{
    public ConfluenceWebappProductHandler(MavenProject project, MavenGoals goals)
    {
        super(project, goals);
    }

    public String getId()
    {
        return "confluence";
    }

    public ProductArtifact getArtifact()
    {
        return new ProductArtifact("com.atlassian.confluence", "confluence-webapp", "RELEASE");
    }

    public ProductArtifact getTestResourcesArtifact()
    {
        return new ProductArtifact("com.atlassian.confluence.plugins", "confluence-plugin-test-resources", "LATEST");
    }

    public int getDefaultHttpPort()
    {
        return 1990;
    }

    public Map<String, String> getSystemProperties()
    {
        return Collections.singletonMap("confluence.home", getHomeDirectory().getPath());
    }

    public File getPluginsDirectory(final String webappDir, File homeDir)
    {
        // indicates plugins should be bundled
        return null;
    }

    public List<ProductArtifact> getExtraContainerDependencies()
    {
        return Collections.emptyList();
    }

    public String getBundledPluginPath()
    {
        return "WEB-INF/classes/com/atlassian/confluence/setup/atlassian-bundled-plugins.zip";
    }

    public File getHomeDirectory()
    {
        return new File(new File(project.getBuild().getDirectory(), getId()), "confluence-home");
    }

    public void processHomeDirectory(ProductContext ctx, File homeDir) throws MojoExecutionException
    {
        ConfigFileUtils.replace(new File(homeDir, "confluence.cfg.xml"), "@project-dir@", homeDir.getParent());

        File script = new File(new File(homeDir, "database"), "confluencedb.script");
        if (script.exists())
        {
            ConfigFileUtils.replace(new File(new File(homeDir, "database"), "confluencedb.script"),
                    "<baseUrl>http://localhost:8080</baseUrl>",
                    "<baseUrl>http://" + ctx.getServer() + ":" + ctx.getHttpPort() + "/" + ctx.getContextPath().replaceAll("^/|/$", "") + "</baseUrl>");
        }
        else
        {
            ConfigFileUtils.replace(new File(new File(homeDir, "database"), "confluencedb.log"),
                    "<baseUrl>http://localhost:8080</baseUrl>",
                    "<baseUrl>http://" + ctx.getServer() + ":" + ctx.getHttpPort() + "/" + ctx.getContextPath().replaceAll("^/|/$", "") + "</baseUrl>");
        }
    }

    public List<ProductArtifact> getDefaultPlugins()
    {
        return Collections.emptyList();
    }

    public List<ProductArtifact> getDefaultLibPlugins()
    {
        return Collections.emptyList();
    }

    public List<ProductArtifact> getDefaultBundledPlugins()
    {
        return Collections.emptyList();
    }

}
