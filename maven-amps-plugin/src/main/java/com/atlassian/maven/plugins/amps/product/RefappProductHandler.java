package com.atlassian.maven.plugins.amps.product;

import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.util.VersionUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.*;

public class RefappProductHandler extends AbstractWebappProductHandler
{
    public RefappProductHandler(MavenProject project, MavenGoals goals)
    {
        super(project, goals, new RefappPluginProvider());
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
        return null;
    }

    protected List<ProductArtifact> getExtraContainerDependencies()
    {
        return Collections.emptyList();
    }

    protected String getBundledPluginPath(Product ctx)
    {
        return "WEB-INF/classes/atlassian-bundled-plugins.zip";
    }

    protected void processHomeDirectory(Product ctx, File homeDir) throws MojoExecutionException
    {
    }

    protected List<ProductArtifact> getDefaultLibPlugins()
    {
        return Collections.emptyList();
    }

    protected List<ProductArtifact> getDefaultBundledPlugins()
    {
        return Collections.emptyList();
    }

    protected Map<String, String> getSystemProperties(Product ctx)
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("refapp.home", getHomeDirectory(ctx).getPath());
        map.put("osgi.cache", getHomeDirectory(ctx).getPath()+ "/osgi-cache");
        map.put("bundledplugins.cache", getHomeDirectory(ctx).getPath()+ "/bundled-plugins");
        return map;
    }

    protected ProductArtifact getArtifact()
    {
        return new ProductArtifact("com.atlassian.refapp", "atlassian-refapp", VersionUtils.getVersion());
    }

    protected ProductArtifact getTestResourcesArtifact()
    {
        return null;
    }

    private static class RefappPluginProvider extends AbstractPluginProvider
    {

        @Override
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

        @Override
        protected Collection<ProductArtifact> getPdkInstallArtifacts(String pdkInstallVersion)
        {
            return Collections.emptyList();
        }
    }
}
