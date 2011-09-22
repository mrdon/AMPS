package com.atlassian.maven.plugins.amps.product;

import com.atlassian.maven.plugins.amps.MavenContext;
import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.util.ConfigFileUtils.Replacement;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ConfluenceProductHandler extends AbstractWebappProductHandler
{

    public ConfluenceProductHandler(MavenContext context, MavenGoals goals)
    {
        super(context, goals, new ConfluencePluginProvider());
    }

    public String getId()
    {
        return "confluence";
    }

    @Override
    protected boolean isStaticPlugin()
    {
        // assume all Confluence plugins should be installed as bundled plugins -- a pretty good assumption
        return false;
    }

    @Override
    public ProductArtifact getArtifact()
    {
        return new ProductArtifact("com.atlassian.confluence", "confluence-webapp", "RELEASE");
    }

    @Override
    public ProductArtifact getTestResourcesArtifact()
    {
        return new ProductArtifact("com.atlassian.confluence.plugins", "confluence-plugin-test-resources");
    }

    public int getDefaultHttpPort()
    {
        return 1990;
    }

    @Override
    public Map<String, String> getSystemProperties(Product ctx)
    {
        return Collections.singletonMap("confluence.home", getHomeDirectory(ctx).getPath());
    }

    @Override
    public File getUserInstalledPluginsDirectory(final File webappDir, File homeDir)
    {
        // indicates plugins should be bundled
        return null;
    }

    @Override
    public List<ProductArtifact> getExtraContainerDependencies()
    {
        return Collections.emptyList();
    }

    @Override
    public String getBundledPluginPath(Product ctx)
    {
        return "WEB-INF/classes/com/atlassian/confluence/setup/atlassian-bundled-plugins.zip";
    }

    @Override
    public List<Replacement> getReplacements(Product ctx)
    {
        List<Replacement> replacements = super.getReplacements(ctx);
        File homeDir = getHomeDirectory(ctx);
        // We don't rewrap homes with these values:
        replacements.add(new Replacement("@project-dir@", homeDir.getParent()));
        replacements.add(new Replacement("/confluence-home/", "/home/", false));
        replacements.add(new Replacement("<baseUrl>http://localhost:8080</baseUrl>", "<baseUrl>http://" + ctx.getServer() + ":" + ctx.getHttpPort() + "/" + ctx.getContextPath().replaceAll("^/|/$", "") + "</baseUrl>", false));
        return replacements;
    }

    @Override
    public List<File> getConfigFiles(Product product, File homeDirectory)
    {
        List<File> configFiles = super.getConfigFiles(product, homeDirectory);
        File script = new File(new File(homeDirectory, "database"), "confluencedb.script");
        if (!script.exists())
        {
            script = new File(new File(homeDirectory, "database"), "confluencedb.log");
        }

        configFiles.add(script);
        configFiles.add(new File(homeDirectory, "confluence.cfg.xml"));
        return configFiles;
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

    private static class ConfluencePluginProvider extends AbstractPluginProvider
    {

        @Override
        protected Collection<ProductArtifact> getSalArtifacts(String salVersion)
        {
            return Arrays.asList(
                new ProductArtifact("com.atlassian.sal", "sal-api", salVersion),
                new ProductArtifact("com.atlassian.sal", "sal-confluence-plugin", salVersion));
        }

        @Override
        protected Collection<ProductArtifact> getPdkInstallArtifacts(String pdkInstallVersion)
        {
            return Collections.emptyList();
        }
    }

    @Override
    public void cleanupProductHomeForZip(Product product, File homeDirectory) throws MojoExecutionException, IOException
    {
        super.cleanupProductHomeForZip(product, homeDirectory);
        FileUtils.deleteDirectory(new File(homeDirectory, "plugins-osgi-cache"));
        FileUtils.deleteDirectory(new File(homeDirectory, "plugins-temp"));
    }
}
