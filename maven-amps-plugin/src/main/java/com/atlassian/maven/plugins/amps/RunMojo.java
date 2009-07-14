package com.atlassian.maven.plugins.amps;

import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import com.atlassian.maven.plugins.amps.product.ProductHandler;

/**
 * Run the webapp
 * @requiresDependencyResolution runtime
 * @goal run
 * @execute phase="package"
 */
public class RunMojo extends AbstractProductHandlerMojo
{
    protected void doExecute() throws MojoExecutionException, MojoFailureException
    {
        final MavenGoals goals = getMavenGoals();
        ProductHandler product = createProductHandler();
        Product ctx = getProductContexts(goals).get(0);

        int actualHttpPort = product.start(ctx);

        getLog().info(product.getId() + " started successfully and available at http://localhost:" + actualHttpPort + ctx.getContextPath());
        getLog().info("Type CTRL-C to exit");
        try
        {
            while (System.in.read() != (char) 27)
            {
            }
        }
        catch (final IOException e)
        {
            // ignore
        }
    }
}
