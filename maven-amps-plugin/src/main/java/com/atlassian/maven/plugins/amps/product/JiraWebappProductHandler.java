package com.atlassian.maven.plugins.amps.product;

import org.apache.maven.project.MavenProject;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.io.File;

import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.ProductContext;
import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.util.ConfigFileUtils;

public class JiraWebappProductHandler extends AbstractWebappProductHandler
{
    public JiraWebappProductHandler(MavenProject project, MavenGoals goals)
    {
        super(project, goals);
    }

    public String getId()
    {
        return "jira";
    }

    public ProductArtifact getArtifact()
    {
        return new ProductArtifact("com.atlassian.jira", "atlassian-jira-webapp", "RELEASE");
    }

    public ProductArtifact getTestResourcesArtifact()
    {
        return new ProductArtifact("com.atlassian.jira.plugins", "jira-plugin-test-resources", "LATEST");
    }

    public int getDefaultHttpPort()
    {
        return 2990;
    }

    public Map<String, String> getSystemProperties()
    {
        return new HashMap<String, String>()
        {{
                put("jira.home", getHomeDirectory().getPath());
                put("cargo.datasource.datasource",
                        "cargo.datasource.url=jdbc:hsqldb:" + project.getBuild().getDirectory() + "/" + getId() + "/jira-home/database|" +
                                "cargo.datasource.driver=org.hsqldb.jdbcDriver|" +
                                "cargo.datasource.username=sa|" +
                                "cargo.datasource.password=|" +
                                "cargo.datasource.type=javax.sql.DataSource|" +
                                "cargo.datasource.jndi=jdbc/JiraDS");
            }};
    }

    public Collection<ProductArtifact> getSalArtifacts(final String salVersion)
    {
        return Arrays.asList(
                new ProductArtifact("com.atlassian.sal", "sal-api", salVersion),
                new ProductArtifact("com.atlassian.sal", "sal-jira-plugin", salVersion));
    }

    public File getPluginsDirectory(final String webappDir, File homeDir)
    {
        return new File(new File(homeDir, "plugins"), "installed-plugins");
    }

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

    public String getBundledPluginPath()
    {
        return "WEB-INF/classes/com/atlassian/jira/plugin/atlassian-bundled-plugins.zip";
    }

    public File getHomeDirectory()
    {
        return new File(new File(project.getBuild().getDirectory(), getId()), "jira-home");
    }

    public void processHomeDirectory(ProductContext ctx, File homeDir) throws MojoExecutionException
    {
        ConfigFileUtils.replace(new File(homeDir, "database.script"), "@project-dir@", homeDir.getParent());
    }

    public List<ProductArtifact> getDefaultPlugins()
    {
        return Collections.singletonList(new ProductArtifact("com.atlassian.pdkinstall", "pdkinstall-plugin", "0.2"));
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
