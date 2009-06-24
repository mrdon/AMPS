package com.atlassian.maven.plugins.refapp;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;

/**
 * Run the integration tests against the webapp
 *
 * @requiresDependencyResolution integration-test
 * @goal integration-test
 */
public class IntegrationTestMojo
        extends AbstractWebappMojo
{

    /**
     * Pattern for to use to find integration tests
     *
     * @parameter expression="${functionalTestPattern}"
     */
    private final String functionalTestPattern = "it/**";

    /**
     * The directory containing generated test classes of the project being tested.
     *
     * @parameter expression="${project.build.testOutputDirectory}"
     * @required
     */
    private File testClassesDirectory;

    /**
     * Whether the reference application will not be started or not
     *
     * @parameter expression="${noWebapp}"
     */
    private final boolean noWebapp = false;

    /**
     * @component
     */
    private ArtifactHandlerManager artifactHandlerManager;

    protected void doExecute() throws MojoExecutionException
    {

        // workaround for MNG-1682/MNG-2426: force maven to install artifact using the "jar" handler
        project.getArtifact().setArtifactHandler(artifactHandlerManager.getArtifactHandler("jar"));

        if (!new File(testClassesDirectory, "it").exists())
        {
            getLog().info("No integration tests found");
            return;
        }
        MavenGoals goals = new MavenGoals(new MavenContext(project, session, pluginManager, getLog()), getWebappHandler());

        final String pluginJar = targetDirectory.getAbsolutePath() + "/" + finalName + ".jar";

        int actualHttpPort = 0;
        if (!noWebapp)
        {
            // Copy the webapp war to target
            final File webappWar = goals.copyWebappWar(getBaseDirectory(), getVersion());

            File homeDir = extractAndProcessHomeDirectory(goals);

            final File combinedWebappWar = addArtifacts(goals, homeDir, webappWar);

            actualHttpPort = goals.startWebapp(createWebappContext(combinedWebappWar));

        }
        goals.runTests(containerId, functionalTestPattern, actualHttpPort, contextPath, pluginJar);

        if (!noWebapp)
        {
            goals.stopWebapp(containerId);
        }

    }
}