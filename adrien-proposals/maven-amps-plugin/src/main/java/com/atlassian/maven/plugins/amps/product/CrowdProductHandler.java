package com.atlassian.maven.plugins.amps.product;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.atlassian.maven.plugins.amps.Crowd;
import com.atlassian.maven.plugins.amps.MavenContext;
import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.util.ConfigFileUtils;

import com.google.common.collect.ImmutableMap;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;

public class CrowdProductHandler extends AbstractWebappProductHandler
{
    public CrowdProductHandler(final MavenContext context, final MavenGoals goals)
    {
        super(context, goals, new CrowdPluginProvider());
    }

    public String getId()
    {
        return Crowd.ID;
    }

    @Override
    public ProductArtifact getArtifact()
    {
        return new ProductArtifact("com.atlassian.crowd", "crowd-web-app", "RELEASE");
    }

    @Override
    public ProductArtifact getTestResourcesArtifact()
    {
        return new ProductArtifact("com.atlassian.crowd.distribution", "crowd-plugin-test-resources");
    }

    public int getDefaultHttpPort()
    {
        return 4990;
    }

    @Override
    public Map<String, String> getSystemProperties(final Product ctx)
    {
        return ImmutableMap.of("crowd.home", getHomeDirectory(ctx).getPath());
    }

    private static String slashPrefixed(String contextPath)
    {
        if (!contextPath.startsWith("/"))
        {
            contextPath = "/" + contextPath;
        }

        return contextPath;
    }

    @Override
    public File getUserInstalledPluginsDirectory(final File webappDir, final File homeDir)
    {
        return new File(homeDir, "plugins");
    }

    @Override
    public List<ProductArtifact> getExtraContainerDependencies()
    {
        return Arrays.asList(
                new ProductArtifact("hsqldb", "hsqldb", "1.8.0.7"),
                new ProductArtifact("jta", "jta", "1.0.1B"),
                new ProductArtifact("javax.mail", "mail", "1.4"),
                new ProductArtifact("javax.activation", "activation", "1.0.2")
        );
    }

    @Override
    public String getBundledPluginPath(Product ctx)
    {
        return "WEB-INF/classes/atlassian-bundled-plugins.zip";
    }

    @Override
    public void processHomeDirectory(final Product ctx, final File homeDir) throws MojoExecutionException
    {
        String baseUrl = MavenGoals.getBaseUrl(ctx.getServer(), ctx.getHttpPort(), slashPrefixed(ctx.getContextPath()));

        try
        {
            ConfigFileUtils.replaceAll(new File(homeDir, "crowd.cfg.xml"),
                    "jdbc:hsqldb:.*/(crowd-)?home/database/defaultdb",
                    "jdbc:hsqldb:" + getHomeDirectory(ctx).getCanonicalPath().replace("\\", "/") + "/database/defaultdb");

            Map<String, String> newProperties = ImmutableMap.of(
                    "crowd.server.url", baseUrl + "/services",
                    "application.login.url", baseUrl
            );
            ConfigFileUtils.setProperties(new File(homeDir, "crowd.properties"), newProperties);
        }
        catch (final IOException e)
        {
            throw new MojoExecutionException(e.getMessage());
        }
    }

    @Override
    public List<ProductArtifact> getDefaultLibPlugins()
    {
        return Collections.emptyList();
    }

    @Override
    public List<ProductArtifact> getDefaultBundledPlugins()
    {
        return Collections.emptyList();
    }

    private static class CrowdPluginProvider extends AbstractPluginProvider
    {

        @Override
        protected Collection<ProductArtifact> getSalArtifacts(final String salVersion)
        {
            return Arrays.asList(
                    new ProductArtifact("com.atlassian.sal", "sal-api", salVersion),
                    new ProductArtifact("com.atlassian.sal", "sal-crowd-plugin", salVersion));
        }

        @Override
        protected Collection<ProductArtifact> getPdkInstallArtifacts(final String pdkInstallVersion)
        {
            final List<ProductArtifact> plugins = new ArrayList<ProductArtifact>();
            plugins.addAll(super.getPdkInstallArtifacts(pdkInstallVersion));
            plugins.add(new ProductArtifact("commons-fileupload", "commons-fileupload", "1.2.1"));
            return plugins;
        }
    }

    @Override
    public void cleanupProductHomeForZip(Product product, File homeDirectory) throws MojoExecutionException, IOException
    {
        super.cleanupProductHomeForZip(product, homeDirectory);
        FileUtils.deleteQuietly(new File(homeDirectory, "caches/transformed-plugins"));
        FileUtils.deleteQuietly(new File(homeDirectory, "caches/felix/felix-cache"));
        FileUtils.deleteQuietly(new File(homeDirectory, "logs"));
    }

    @Override
    public List<File> getConfigFiles(Product product, File snapshotDir)
    {
        List<File> configFiles = super.getConfigFiles(product, snapshotDir);
        configFiles.add(new File(snapshotDir, "database.log"));
        configFiles.add(new File(snapshotDir, "crowd.cfg.xml"));
        configFiles.add(new File(snapshotDir, "crowd.properties"));
        return configFiles;
    }

}
