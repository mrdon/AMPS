package com.atlassian.maven.plugins.amps;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.DataInputStream;

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
        extends AbstractProductMojo
{
    protected void doExecute() throws MojoExecutionException, MojoFailureException
    {
        final MavenGoals goals = new MavenGoals(new MavenContext(project, session, pluginManager, getLog()));

        ProductHandler product = createProductHandler(goals);
        ProductContext ctx = createProductContext(product);

        int actualHttpPort = product.start(ctx);

        getLog().info(product.getId() + " started successfully and available at http://localhost:" + actualHttpPort + ctx.getContextPath());
        getLog().info("Type CTRL-C to exit");
        try
        {
            while (System.in.read() != (char)27)
            {
            }
        }
        catch (final IOException e)
        {
            // ignore
        }
    }
}
