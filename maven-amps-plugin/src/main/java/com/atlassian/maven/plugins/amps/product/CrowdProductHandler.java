package com.atlassian.maven.plugins.amps.product;

import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.util.ConfigFileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CrowdProductHandler extends AbstractWebappProductHandler
{
    public CrowdProductHandler(MavenProject project, MavenGoals goals)
    {
        super(project, goals);
    }

    public String getId()
    {
        return ProductHandlerFactory.CROWD;
    }

    public ProductArtifact getArtifact()
    {
        return new ProductArtifact("com.atlassian.crowd", "crowd-web-app", "RELEASE");
    }

    protected Collection<ProductArtifact> getSalArtifacts(String salVersion)
    {
        return Arrays.asList(
                new ProductArtifact("com.atlassian.sal", "sal-api", salVersion),
                new ProductArtifact("com.atlassian.sal", "sal-crowd-plugin", salVersion));
    }

    public ProductArtifact getTestResourcesArtifact()
    {
        return new ProductArtifact("com.atlassian.crowd.distribution", "crowd-plugin-test-resources", "LATEST");
    }

    public int getDefaultHttpPort()
    {
        return 4990;
    }

    @Override
    public Map<String, String> getSystemProperties(final Product ctx)
    {
        return new HashMap<String, String>()
        {{
            put("crowd.home", getHomeDirectory().getPath());

            String contextPath = ctx.getContextPath();
            if (!contextPath.startsWith("/"))
            {
                contextPath = "/" + contextPath;
            }
            if (!contextPath.endsWith("/"))
            {
                contextPath = contextPath + "/";
            }

            put("crowd.property.crowd.server.url", "http://" +  ctx.getServer() + ":" + ctx.getHttpPort() + contextPath + "services/");
        }};
    }

    public File getPluginsDirectory(final String webappDir, final File homeDir)
    {
        // indicates plugins should be bundled
        return null;
    }

    public List<ProductArtifact> getExtraContainerDependencies()
    {
        return Arrays.asList(
                new ProductArtifact("hsqldb", "hsqldb", "1.8.0.7"),
                new ProductArtifact("jta", "jta", "1.0.1B"),
                new ProductArtifact("javax.mail", "mail", "1.4"),
                new ProductArtifact("javax.activation", "activation", "1.0.2")
        );
    }

    public String getBundledPluginPath()
    {
        return "WEB-INF/classes/atlassian-bundled-plugins.zip";
    }

    public void processHomeDirectory(final Product ctx, final File homeDir) throws MojoExecutionException
    {
        try
        {
            ConfigFileUtils.replaceAll(new File(homeDir, "crowd.cfg.xml"),
                    "jdbc:hsqldb:.*/crowd-home/database/defaultdb",
                    "jdbc:hsqldb:" + getHomeDirectory().getCanonicalPath() + "/database/defaultdb");
        }
        catch (IOException e)
        {
            throw new MojoExecutionException(e.getMessage());
        }
    }

    public List<ProductArtifact> getDefaultPlugins()
    {
        return Arrays.asList(
                new ProductArtifact("com.atlassian.pdkinstall", "pdkinstall-plugin", "0.4"),
                new ProductArtifact("commons-fileupload", "commons-fileupload", "1.2.1")
        );
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
