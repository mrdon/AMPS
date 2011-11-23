package com.atlassian.maven.plugins.amps.product;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;

import com.atlassian.maven.plugins.amps.MavenContext;
import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.util.ConfigFileUtils.Replacement;

public class CaviarProductHandler extends AbstractWebappProductHandler
{
    public CaviarProductHandler(final MavenContext context, final MavenGoals goals)
    {
        super(context, goals, new CaviarPluginProvider());
    }

    public String getId()
    {
        return ProductHandlerFactory.CAVIAR;
    }

    @Override
    public ProductArtifact getArtifact()
    {
        return new ProductArtifact("com.atlassian.caviar", "caviar-webapp");
    }

    @Override
    public ProductArtifact getTestResourcesArtifact()
    {
        return new ProductArtifact("com.atlassian.caviar", "caviar-plugin-test-resources");
    }

    public int getDefaultHttpPort()
    {
        return 7990;
    }
/*
    protected static File getHsqlDatabaseFile(final File homeDirectory)
    {
        return new File(homeDirectory, "database");
    }*/

    @Override
    public Map<String, String> getSystemProperties(final Product ctx)
    {
        return new HashMap<String, String>()
        {
            {
                /*final String dburl = System.getProperty("amps.datasource.url", format("jdbc:hsqldb:%s/database", fixSlashes(getHomeDirectory(ctx).getAbsolutePath())));
                final String driverClass = System.getProperty("amps.datasource.driver", "org.hsqldb.jdbcDriver");
                final String username = System.getProperty("amps.datasource.username", "sa");
                final String password = System.getProperty("amps.datasource.password", "");
                final String datasourceTypeClass = "javax.sql.DataSource";

                final String datasource = format("cargo.datasource.url=%s", dburl);
                final String driver = format("cargo.datasource.driver=%s", driverClass);
                final String datasourceUsername = format("cargo.datasource.username=%s", username);
                final String datasourcePassword = format("cargo.datasource.password=%s", password);
                final String datasourceType = "cargo.datasource.type=" + datasourceTypeClass;
                final String jndi = "cargo.datasource.jndi=jdbc/CaviarDS";*/

                put("caviar.home", fixSlashes(getHomeDirectory(ctx).getPath()));

                String baseUrl = MavenGoals.getBaseUrl(ctx, ctx.getHttpPort());
                put("baseurl", baseUrl);
                put("baseurl.display", baseUrl);
                /*put("cargo.datasource.datasource", format("%s|%s|%s|%s|%s|%s", datasource, driver, datasourceUsername, datasourcePassword, datasourceType, jndi));*/
            }
        };
    }

    private static String fixSlashes(final String path)
    {
        return path.replaceAll("\\\\", "/");
    }

    @Override
    public File getUserInstalledPluginsDirectory(final File webappDir, final File homeDir)
    {
        return new File(new File(homeDir, "plugins"), "installed-plugins");
    }

    @Override
    public List<ProductArtifact> getExtraContainerDependencies()
    {
        return Arrays.asList(
                /*new ProductArtifact("hsqldb", "hsqldb", "1.8.0.5"),
                new ProductArtifact("jta", "jta", "1.0.1"),
                new ProductArtifact("ots-jts", "ots-jts", "1.0"),

                // for data source and transaction manager providers
                new ProductArtifact("jotm", "jotm", "1.4.3"),
                new ProductArtifact("jotm", "jotm-jrmp_stubs", "1.4.3"),
                new ProductArtifact("jotm", "jotm-iiop_stubs", "1.4.3"),
                new ProductArtifact("jotm", "jonas_timer", "1.4.3"),
                new ProductArtifact("jotm", "objectweb-datasource", "1.4.3"),
                new ProductArtifact("carol", "carol", "1.5.2"),
                new ProductArtifact("carol", "carol-properties", "1.0"),
                new ProductArtifact("xapool", "xapool", "1.3.1"),
                new ProductArtifact("commons-logging", "commons-logging", "1.1.1")*/
        );
    }

    @Override
    public String getBundledPluginPath(Product ctx)
    {
        String bundledPluginPluginsPath = "WEB-INF/classes/caviar-bundled-plugins.zip";
        return bundledPluginPluginsPath;
    }

    @Override
    public List<Replacement> getReplacements(Product ctx)
    {
        List<Replacement> replacements = super.getReplacements(ctx);
        return replacements;
    }

    @Override
    public List<File> getConfigFiles(Product product, File homeDir)
    {
        List<File> configFiles = super.getConfigFiles(product, homeDir);
        configFiles.add(new File(homeDir, "data/db.log"));
        configFiles.add(new File(homeDir, "data/db.script"));
        configFiles.add(new File(homeDir, "data/db.properties"));
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

    private static class CaviarPluginProvider extends AbstractPluginProvider
    {

        @Override
        protected Collection<ProductArtifact> getSalArtifacts(String salVersion)
        {
            return Arrays.asList(
                new ProductArtifact("com.atlassian.sal", "sal-api", salVersion),
                new ProductArtifact("com.atlassian.sal", "caviar-sal", salVersion)
                );
        }

        @Override
        protected Collection<ProductArtifact> getPdkInstallArtifacts(String pdkInstallVersion)
        {
            List<ProductArtifact> plugins = new ArrayList<ProductArtifact>();
            plugins.addAll(super.getPdkInstallArtifacts(pdkInstallVersion));
            //plugins.add(new ProductArtifact("commons-fileupload", "commons-fileupload", "1.2.1"));
            return plugins;
        }
    }

    @Override
    public void cleanupProductHomeForZip(Product product, File snapshotDir) throws MojoExecutionException, IOException
    {
        super.cleanupProductHomeForZip(product, snapshotDir);
        FileUtils.deleteQuietly(new File(snapshotDir, "log/atlassian-caviar.log"));
        FileUtils.deleteQuietly(new File(snapshotDir, ".osgi-cache"));
    }


}
