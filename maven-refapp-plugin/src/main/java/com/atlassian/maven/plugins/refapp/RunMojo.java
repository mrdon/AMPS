package com.atlassian.maven.plugins.refapp;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Run the webapp
 *
 * @requiresDependencyResolution run
 * @goal run
 * @execute phase="package"
 */
public class RunMojo
        extends AbstractWebappMojo
{
    protected void doExecute() throws MojoExecutionException, MojoFailureException
    {
        final MavenGoals goals = new MavenGoals(new MavenContext(project, session, pluginManager, getLog()), getWebappHandler());

        // Copy the webapp war to target
        final File webappWar = goals.copyWebappWar(getBaseDirectory(), getVersion());

        File homeDir = extractAndProcessHomeDirectory(goals);

        final File combinedWebappWar = addArtifacts(goals, homeDir, webappWar);

        // Start the refapp
        final int actualHttpPort = goals.startWebapp(createWebappContext(combinedWebappWar));

        getLog().info(getWebappHandler().getId() + " started successfully and available at http://localhost:" + actualHttpPort + contextPath);
        getLog().info("Type any key to exit");
        try
        {
            System.in.read();
        }
        catch (final IOException e)
        {
            // ignore
        }
    }
}
