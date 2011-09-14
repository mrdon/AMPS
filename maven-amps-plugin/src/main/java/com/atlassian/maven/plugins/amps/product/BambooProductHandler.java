package com.atlassian.maven.plugins.amps.product;

import com.atlassian.maven.plugins.amps.MavenContext;
import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.util.ConfigFileUtils;
import com.atlassian.maven.plugins.amps.util.ConfigFileUtils.Replacement;
import org.apache.maven.plugin.MojoExecutionException;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.atlassian.maven.plugins.amps.util.FileUtils.deleteDir;

public class BambooProductHandler extends AbstractWebappProductHandler
{
    public BambooProductHandler(MavenContext context, MavenGoals goals)
    {
        super(context, goals, new BambooPluginProvider());
    }

    public String getId()
    {
        return "bamboo";
    }

    public ProductArtifact getArtifact()
    {
        return new ProductArtifact("com.atlassian.bamboo", "atlassian-bamboo-web-app", "RELEASE");
    }

    protected Collection<ProductArtifact> getSalArtifacts(String salVersion)
    {
        return Arrays.asList(
                new ProductArtifact("com.atlassian.sal", "sal-api", salVersion),
                new ProductArtifact("com.atlassian.sal", "sal-bamboo-plugin", salVersion));
    }

    public ProductArtifact getTestResourcesArtifact()
    {
        return new ProductArtifact("com.atlassian.bamboo.plugins", "bamboo-plugin-test-resources");
    }

    public int getDefaultHttpPort()
    {
        return 6990;
    }

    public Map<String, String> getSystemProperties(Product ctx)
    {
        Map<String, String> systemProperties = new HashMap<String, String>();
        systemProperties.put("bamboo.home", getHomeDirectory(ctx).getPath());
        systemProperties.put("org.apache.catalina.loader.WebappClassLoader.ENABLE_CLEAR_REFERENCES", "false");
        return Collections.unmodifiableMap(systemProperties);
    }

    @Override
    public File getUserInstalledPluginsDirectory(final File webappDir, final File homeDir)
    {
        return new File(homeDir, "plugins");
    }

    public List<ProductArtifact> getExtraContainerDependencies()
    {
        return Collections.emptyList();
    }

    public String getBundledPluginPath(Product ctx)
    {
        return "WEB-INF/classes/atlassian-bundled-plugins.zip";
    }

    public void processHomeDirectory(final Product ctx, final File homeDir) throws MojoExecutionException
    {
        super.processHomeDirectory(ctx, homeDir);

        // The regex in the following search text is used to match IPv4 ([^:]+) or IPv6 (\[.+]) addresses.
        ConfigFileUtils.replaceAll(new File(homeDir, "/xml-data/configuration/administration.xml"),
                "http://(?:[^:]+|\\[.+]):8085", "http://" + ctx.getServer() + ":" + ctx.getHttpPort() + "/" + ctx.getContextPath().replaceAll("^/|/$", ""));
    }



    @Override
    public List<Replacement> getReplacements(Product product)
    {
        List<Replacement> replacements = super.getReplacements(product);
        File homeDirectory = getHomeDirectory(product);
        replacements.add(new Replacement("@project-dir@", homeDirectory.getParent()));
        replacements.add(new Replacement("/bamboo-home/", "/home/"));
        replacements.add(new Replacement("${bambooHome}", homeDirectory.getAbsolutePath()));
        return replacements;
    }

    @Override
    public List<File> getConfigFiles(Product product, File homeDirectory)
    {
        List<File> configFiles = super.getConfigFiles(product, homeDirectory);
        configFiles.add(new File(homeDirectory, "bamboo.cfg.xml"));
        configFiles.add(new File(homeDirectory, "database/defaultdb.log"));
        configFiles.add(new File(homeDirectory, "database/defaultdb.script"));
        configFiles.add(new File(homeDirectory, "database.log"));
        configFiles.add(new File(homeDirectory, "database.script"));
        return configFiles;
    }

    public List<ProductArtifact> getDefaultLibPlugins()
    {
        return Collections.emptyList();
    }

    public List<ProductArtifact> getDefaultBundledPlugins()
    {
        return Collections.emptyList();
    }

    @Override
    public void cleanupProductHomeForZip(Product bamboo, File genDir) throws MojoExecutionException, IOException
    {
        super.cleanupProductHomeForZip(bamboo, genDir);
        deleteDir(new File(genDir, "jms-store"));
        deleteDir(new File(genDir, "caches"));
        deleteDir(new File(genDir, "logs"));
    }

    private static class BambooPluginProvider extends AbstractPluginProvider
    {

        @Override
        protected Collection<ProductArtifact> getSalArtifacts(String salVersion)
        {
            return Arrays.asList(
                new ProductArtifact("com.atlassian.sal", "sal-api", salVersion),
                new ProductArtifact("com.atlassian.sal", "sal-bamboo-plugin", salVersion));
        }

    }
}
