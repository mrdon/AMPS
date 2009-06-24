package com.atlassian.maven.plugins.refapp;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * Handler for webapp-specific settings
 */
public interface WebappHandler
{
    /**
     * @return the webapp id
     */
    String getId();

    /**
     * @return the webapp artifact
     */
    WebappArtifact getArtifact();

    /**
     * @return the test resources artifact
     */
    WebappArtifact getTestResourcesArtifact();

    int getDefaultHttpPort();

    /**
     * @param project The current project
     * @return the system properties to set when executing the webapp
     */
    Map<String,String> getSystemProperties(MavenProject project);

    /**
     * @param salVersion The sal version
     * @return the list of artifacts to include when the sal version is specified
     */
    Collection<WebappArtifact> getSalArtifacts(String salVersion);

    /**
     * @param webappDir the webapp directory
     * @return the directory to store plugins into.  Return null to force plugins into the bundled plugins zip.
     */
    File getPluginsDirectory(String webappDir, File homeDir);

    List<WebappArtifact> getExtraContainerDependencies();

    /**
     * @return the path to the bundled plugins zip within the webapp
     */
    String getBundledPluginPath();

    void processHomeDirectory(MavenProject project, File homeDir, AbstractWebappMojo webappMojo) throws MojoExecutionException;

    /**
     * Necessary if the plugin doesn't have test resources
     */
    File getHomeDirectory(MavenProject project);
    
    List<WebappArtifact> getDefaultPlugins();

    List<WebappArtifact> getDefaultLibPlugins();

    List<WebappArtifact> getDefaultBundledPlugins();

}
