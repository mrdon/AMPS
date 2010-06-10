package com.atlassian.maven.plugins.amps.product;

import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.util.ConfigFileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.*;

public class JiraProductHandler extends AbstractWebappProductHandler
{
    public JiraProductHandler(final MavenProject project, final MavenGoals goals)
    {
        super(project, goals, new JiraPluginProvider());
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

    @Override
    public Map<String, String> getSystemProperties(final Product ctx)
    {
        return new HashMap<String, String>()
        {
            {
                put("jira.home", fixSlashes(getHomeDirectory(ctx.getInstanceId()).getPath()));
                put("cargo.datasource.datasource", "cargo.datasource.url=jdbc:hsqldb:"
                        + fixSlashes(getHomeDirectory(ctx.getInstanceId()).getAbsolutePath()) + "/database|"
                        + "cargo.datasource.driver=org.hsqldb.jdbcDriver|" + "cargo.datasource.username=sa|"
                        + "cargo.datasource.password=|" + "cargo.datasource.type=javax.sql.DataSource|"
                        + "cargo.datasource.jndi=jdbc/JiraDS");
            }

        };
    }

    private static String fixSlashes(final String path)
    {
        return path.replaceAll("\\\\", "/");
    }

    @Override
    public File getPluginsDirectory(final String webappDir, final File homeDir)
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
    	String[] version = ctx.getVersion().split("-", 2)[0].split("\\.");
    	long major = Long.parseLong(version[0]);
    	long minor = Long.parseLong(version[1]);
    	
    	if (major < 4 || major == 4 && minor == 0)
    	{
    		return "WEB-INF/classes/com/atlassian/jira/plugin/atlassian-bundled-plugins.zip";
    	}
    	else
    	{
    		return "WEB-INF/classes/atlassian-bundled-plugins.zip";
    	}
    }

    @Override
    public void processHomeDirectory(final Product ctx, final File homeDir) throws MojoExecutionException
    {
        ConfigFileUtils.replace(new File(homeDir, "database.script"), "@project-dir@", homeDir.getParent());
        ConfigFileUtils.replace(new File(homeDir, "database.script"), "/jira-home/", "/home/");
        ConfigFileUtils.replace(new File(homeDir, "database.script"), "@base-url@",
                "http://" + ctx.getServer() + ":" + ctx.getHttpPort() + "/" + ctx.getContextPath());
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
