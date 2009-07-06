package com.atlassian.maven.plugins.amps.product;

import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.product.AbstractWebappProductHandler;
import com.atlassian.maven.plugins.amps.util.VersionUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Arrays;

public class RefappProductHandler extends AbstractWebappProductHandler
{
    public RefappProductHandler(MavenProject project, MavenGoals goals)
    {
        super(project, goals);
    }

    public String getId()
    {
        return "refapp";
    }

    public int getDefaultHttpPort()
    {
        return 5990;
    }

    protected File getPluginsDirectory(final String webappDir, File homeDir)
    {
        return new File(webappDir, "WEB-INF/plugins");
    }

    protected List<ProductArtifact> getExtraContainerDependencies()
    {
        return Collections.emptyList();
    }

    protected String getBundledPluginPath()
    {
        return "WEB-INF/classes/atlassian-bundled-plugins.zip";
    }

    protected void processHomeDirectory(Product ctx, File homeDir) throws MojoExecutionException
    {
    }

    protected List<ProductArtifact> getDefaultPlugins()
    {
        return Collections.emptyList();
    }

    protected List<ProductArtifact> getDefaultLibPlugins()
    {
        return Collections.emptyList();
    }

    protected List<ProductArtifact> getDefaultBundledPlugins()
    {
        return Collections.emptyList();
    }

    protected Map<String, String> getSystemProperties()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("refapp.home", getHomeDirectory().getPath());
        map.put("osgi.cache", "${project.build.directory}/osgi-cache");
        return map;
    }

    protected File getHomeDirectory()
    {
        File homeDir = new File(new File(project.getBuild().getDirectory(), getId()), "refapp-home");
        // Make sure it exists
        if (!homeDir.exists())
        {
            homeDir.mkdirs();
        }
        return homeDir;
    }


    protected ProductArtifact getArtifact()
    {
        return new ProductArtifact("com.atlassian.refapp", "atlassian-refapp", VersionUtils.getVersion());
    }

    protected Collection<ProductArtifact> getSalArtifacts(String salVersion)
    {
        return Arrays.asList(
                new ProductArtifact("com.atlassian.sal", "sal-api", salVersion),
                new ProductArtifact("com.atlassian.sal", "sal-refimpl-appproperties-plugin", salVersion),
                new ProductArtifact("com.atlassian.sal", "sal-refimpl-component-plugin", salVersion),
                new ProductArtifact("com.atlassian.sal", "sal-refimpl-executor-plugin", salVersion),
                new ProductArtifact("com.atlassian.sal", "sal-refimpl-lifecycle-plugin", salVersion),
                new ProductArtifact("com.atlassian.sal", "sal-refimpl-message-plugin", salVersion),
                new ProductArtifact("com.atlassian.sal", "sal-refimpl-net-plugin", salVersion),
                new ProductArtifact("com.atlassian.sal", "sal-refimpl-pluginsettings-plugin", salVersion),
                new ProductArtifact("com.atlassian.sal", "sal-refimpl-project-plugin", salVersion),
                new ProductArtifact("com.atlassian.sal", "sal-refimpl-search-plugin", salVersion),
                new ProductArtifact("com.atlassian.sal", "sal-refimpl-transaction-plugin", salVersion),
                new ProductArtifact("com.atlassian.sal", "sal-refimpl-upgrade-plugin", salVersion),
                new ProductArtifact("com.atlassian.sal", "sal-refimpl-user-plugin", salVersion));
    }

    protected ProductArtifact getTestResourcesArtifact()
    {
        return null;
    }
}
