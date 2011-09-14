package com.atlassian.maven.plugins.amps.product;

import com.atlassian.maven.plugins.amps.MavenContext;
import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.util.ConfigFileUtils.Replacement;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static java.lang.String.format;

public class JiraProductHandler extends AbstractWebappProductHandler
{
    public JiraProductHandler(final MavenContext context, final MavenGoals goals)
    {
        super(context, goals, new JiraPluginProvider());
    }

    public String getId()
    {
        return "jira";
    }

    @Override
    public ProductArtifact getArtifact()
    {
        return new ProductArtifact("com.atlassian.jira", "atlassian-jira-webapp", "RELEASE");
    }

    @Override
    public ProductArtifact getTestResourcesArtifact()
    {
        return new ProductArtifact("com.atlassian.jira.plugins", "jira-plugin-test-resources", "LATEST");
    }

    public int getDefaultHttpPort()
    {
        return 2990;
    }

    protected static File getHsqlDatabaseFile(final File homeDirectory)
    {
        return new File(homeDirectory, "database");
    }

    @Override
    public Map<String, String> getSystemProperties(final Product ctx)
    {
        return new HashMap<String, String>()
        {
            {
                final String dburl = System.getProperty("amps.datasource.url", format("jdbc:hsqldb:%s/database", fixSlashes(getHomeDirectory(ctx).getAbsolutePath())));
                final String driverClass = System.getProperty("amps.datasource.driver", "org.hsqldb.jdbcDriver");
                final String username = System.getProperty("amps.datasource.username", "sa");
                final String password = System.getProperty("amps.datasource.password", "");
                final String datasourceTypeClass = "javax.sql.DataSource";

                final String datasource = format("cargo.datasource.url=%s", dburl);
                final String driver = format("cargo.datasource.driver=%s", driverClass);
                final String datasourceUsername = format("cargo.datasource.username=%s", username);
                final String datasourcePassword = format("cargo.datasource.password=%s", password);
                final String datasourceType = "cargo.datasource.type=" + datasourceTypeClass;
                final String jndi = "cargo.datasource.jndi=jdbc/JiraDS";

                put("jira.home", fixSlashes(getHomeDirectory(ctx).getPath()));
                put("cargo.datasource.datasource", format("%s|%s|%s|%s|%s|%s", datasource, driver, datasourceUsername, datasourcePassword, datasourceType, jndi));
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
                new ProductArtifact("hsqldb", "hsqldb", "1.8.0.5"),
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
                new ProductArtifact("commons-logging", "commons-logging", "1.1.1")
        );
    }

    @Override
    public String getBundledPluginPath(Product ctx)
    {
        // this location used from 4.1 onwards (inclusive)
        String bundledPluginPluginsPath = "WEB-INF/classes/atlassian-bundled-plugins.zip";

    	String[] version = ctx.getVersion().split("-", 2)[0].split("\\.");
        try
        {
            long major = Long.parseLong(version[0]);
            long minor = (version.length > 1) ? Long.parseLong(version[1]) : 0;

            if (major < 4 || major == 4 && minor == 0)
            {
                bundledPluginPluginsPath = "WEB-INF/classes/com/atlassian/jira/plugin/atlassian-bundled-plugins.zip";
            }
        }
        catch (NumberFormatException e)
        {
            log.debug(String.format("Unable to parse JIRA version '%s', assuming JIRA 4.1 or newer.", ctx.getVersion()), e);
        }

        return bundledPluginPluginsPath;
    }

    @Override
    public void processHomeDirectory(final Product ctx, final File homeDir) throws MojoExecutionException
    {
        super.processHomeDirectory(ctx, homeDir);
        createDbConfigXmlIfNecessary(homeDir);
    }

    @Override
    public List<Replacement> getReplacements(Product ctx)
    {
        String contextPath = ctx.getContextPath();
        if (!contextPath.startsWith("/"))
        {
            contextPath = "/" + contextPath;
        }

        List<Replacement> replacements = super.getReplacements(ctx);
        File homeDir = getHomeDirectory(ctx);
        replacements.add(new Replacement("@project-dir@", homeDir.getParent()));
        replacements.add(new Replacement("/jira-home/", "/home/"));
        replacements.add(new Replacement("@base-url@",
                "http://" + ctx.getServer() + ":" + ctx.getHttpPort() + contextPath));
        return replacements;
    }

    @Override
    public List<File> getConfigFiles(Product product, File homeDir)
    {
        List<File> configFiles = super.getConfigFiles(product, homeDir);
        configFiles.add(new File(homeDir, "database.script"));
        return configFiles;
    }

    static void createDbConfigXmlIfNecessary(File homeDir) throws MojoExecutionException
    {
        File dbConfigXml = new File(homeDir, "dbconfig.xml");
        if (dbConfigXml.exists())
        {
            return;
        }

        InputStream templateIn = JiraProductHandler.class.getResourceAsStream("jira-dbconfig-template.xml");
        if (templateIn == null)
        {
            throw new MojoExecutionException("Missing internal resource: jira-dbconfig-template.xml");
        }

        try
        {
            String template = IOUtils.toString(templateIn, "utf-8");

            File dbFile = getHsqlDatabaseFile(homeDir);
            String jdbcUrl = "jdbc:hsqldb:file:" + dbFile.toURI().getPath();
            String result = template.replace("@jdbc-url@", jdbcUrl);
            FileUtils.writeStringToFile(dbConfigXml, result, "utf-8");
        }
        catch (IOException ioe)
        {
            throw new MojoExecutionException("Unable to create dbconfig.xml", ioe);
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

    private static class JiraPluginProvider extends AbstractPluginProvider
    {

        @Override
        protected Collection<ProductArtifact> getSalArtifacts(String salVersion)
        {
            return Arrays.asList(
                new ProductArtifact("com.atlassian.sal", "sal-api", salVersion),
                new ProductArtifact("com.atlassian.sal", "sal-jira-plugin", salVersion));
        }

        @Override
        protected Collection<ProductArtifact> getPdkInstallArtifacts(String pdkInstallVersion)
        {
            List<ProductArtifact> plugins = new ArrayList<ProductArtifact>();
            plugins.addAll(super.getPdkInstallArtifacts(pdkInstallVersion));
            plugins.add(new ProductArtifact("commons-fileupload", "commons-fileupload", "1.2.1"));
            return plugins;
        }
    }
}
