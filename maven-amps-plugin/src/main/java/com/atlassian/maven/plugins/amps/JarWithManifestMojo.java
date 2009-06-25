package com.atlassian.maven.plugins.amps;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;
import org.apache.maven.execution.MavenSession;

import java.util.Map;
import java.util.HashMap;
import java.io.File;

import static com.atlassian.maven.plugins.amps.util.FileUtils.file;

/**
 * @goal jar
 */
public class JarWithManifestMojo extends AbstractMojo
{
    /**
     * The Maven Project Object
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;
    /**
     * The Maven Session Object
     *
     * @parameter expression="${session}"
     * @required
     * @readonly
     */
    protected MavenSession session;
    /**
     * The Maven PluginManager Object
     *
     * @component
     * @required
     */
    protected PluginManager pluginManager;

    public void execute() throws MojoExecutionException, MojoFailureException {
        MavenGoals goals = new MavenGoals(new MavenContext(project, session, pluginManager, getLog()));

        File mf = file(project.getBuild().getOutputDirectory(), "META-INF", "MANIFEST.MF");
        goals.jarWithOptionalManifest(mf.exists());
    }
}
