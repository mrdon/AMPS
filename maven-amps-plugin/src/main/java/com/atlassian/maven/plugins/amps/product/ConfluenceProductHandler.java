package com.atlassian.maven.plugins.amps.product;

import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.util.ConfigFileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.*;

public class ConfluenceProductHandler extends AbstractWebappProductHandler
{
    public ConfluenceProductHandler(MavenProject project, MavenGoals goals)
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

    protected Collection<ProductArtifact> getSalArtifacts(String salVersion)
    {
        return Arrays.asList(
                new ProductArtifact("com.atlassian.sal", "sal-api", salVersion),
                new ProductArtifact("com.atlassian.sal", "sal-confluence-plugin", salVersion));
    }

    public ProductArtifact getTestResourcesArtifact()
    {
        return new ProductArtifact("com.atlassian.confluence.plugins", "confluence-plugin-test-resources", "LATEST");
    }

    public int getDefaultHttpPort()
    {
        return 1990;
    }

    public Map<String, String> getSystemProperties(Product ctx)
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

    public void processHomeDirectory(Product ctx, File homeDir) throws MojoExecutionException
    {
        ConfigFileUtils.replace(new File(homeDir, "confluence.cfg.xml"), "@project-dir@", homeDir.getParent());
        ConfigFileUtils.replace(new File(homeDir, "confluence.cfg.xml"), "/confluence-home/", "/home/");

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
