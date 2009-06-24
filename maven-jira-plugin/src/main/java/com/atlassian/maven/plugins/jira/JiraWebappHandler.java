package com.atlassian.maven.plugins.jira;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import static org.twdata.maven.mojoexecutor.MojoExecutor.goal;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;
import static org.twdata.maven.mojoexecutor.MojoExecutor.name;

import com.atlassian.maven.plugins.refapp.AbstractWebappMojo;
import com.atlassian.maven.plugins.refapp.MavenContext;
import com.atlassian.maven.plugins.refapp.WebappArtifact;
import com.atlassian.maven.plugins.refapp.WebappHandler;
import com.atlassian.maven.plugins.refapp.util.ConfigFileUtils;

public class JiraWebappHandler implements WebappHandler
{
    public String getId()
    {
        return "jira";
    }

    public WebappArtifact getArtifact()
    {
        return new WebappArtifact("com.atlassian.jira", "atlassian-jira-webapp", "RELEASE");
    }

    public WebappArtifact getTestResourcesArtifact()
    {
        return new WebappArtifact("com.atlassian.jira.plugins", "jira-plugin-test-resources", "LATEST");
    }

    public int getDefaultHttpPort()
    {
        return 2990;
    }

    public Map<String, String> getSystemProperties(final MavenProject project)
    {
        return new HashMap<String,String>() {{
            put("jira.home", getHomeDirectory(project).getPath());
            put("cargo.datasource.datasource",
                         "cargo.datasource.url=jdbc:hsqldb:" + project.getBuild().getDirectory() + "/" + getId() + "/jira-home/database|" +
                             "cargo.datasource.driver=org.hsqldb.jdbcDriver|" +
                             "cargo.datasource.username=sa|" +
                             "cargo.datasource.password=|" +
                             "cargo.datasource.type=javax.sql.DataSource|" +
                             "cargo.datasource.jndi=jdbc/JiraDS");
        }};
    }

    public Collection<WebappArtifact> getSalArtifacts(final String salVersion)
    {
        return Arrays.asList(
                new WebappArtifact("com.atlassian.sal", "sal-api", salVersion),
                new WebappArtifact("com.atlassian.sal", "sal-jira-plugin", salVersion));
    }

    public File getPluginsDirectory(final String webappDir, File homeDir)
    {
        return new File(new File(homeDir, "plugins"), "installed-plugins");
    }

    public List<WebappArtifact> getExtraContainerDependencies()
    {
        return Arrays.asList(
                new WebappArtifact("hsqldb", "hsqldb", "1.8.0.5"),
                new WebappArtifact("jta", "jta", "1.0.1"),
                new WebappArtifact("ots-jts", "ots-jts", "1.0"),

                // for data source and transaction manager providers
                new WebappArtifact("jotm", "jotm", "1.4.3"),
                new WebappArtifact("jotm", "jotm-jrmp_stubs", "1.4.3"),
                new WebappArtifact("jotm", "jotm-iiop_stubs", "1.4.3"),
                new WebappArtifact("jotm", "jonas_timer", "1.4.3"),
                new WebappArtifact("jotm", "objectweb-datasource", "1.4.3"),
                new WebappArtifact("carol", "carol", "1.5.2"),
                new WebappArtifact("carol", "carol-properties", "1.0"),
                new WebappArtifact("xapool", "xapool", "1.3.1"),
                new WebappArtifact("commons-logging", "commons-logging", "1.1.1")
            );
    }

    public String getBundledPluginPath()
    {
        return "WEB-INF/classes/com/atlassian/jira/plugin/atlassian-bundled-plugins.zip";
    }

    public File getHomeDirectory(final MavenProject project)
    {
       return new File(new File(project.getBuild().getDirectory(), getId()),  "jira-home");
    }

    public void processHomeDirectory(MavenProject project, File homeDir, AbstractWebappMojo webappMojo) throws MojoExecutionException
    {
        ConfigFileUtils.replace(new File(homeDir, "database.script"), "@project-dir@", homeDir.getParent());
    }

    public List<WebappArtifact> getDefaultPlugins()
    {
        return Collections.singletonList(new WebappArtifact("com.atlassian.pdkinstall", "pdkinstall-plugin", "0.2"));
    }

    public List<WebappArtifact> getDefaultLibPlugins()
    {
        return Collections.emptyList();
    }

    public List<WebappArtifact> getDefaultBundledPlugins()
    {
        return Collections.emptyList();
    }
}
