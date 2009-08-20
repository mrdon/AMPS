package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.product.ProductHandler;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jfrog.maven.annomojo.annotations.MojoExecute;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;
import org.jfrog.maven.annomojo.annotations.MojoRequiresDependencyResolution;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Run the webapp
 */
@MojoGoal ("run")
@MojoExecute (phase = "package")
@MojoRequiresDependencyResolution
public class RunMojo extends AbstractProductHandlerMojo
{
    private static final char CONTROL_C = (char) 27;

    @MojoParameter (expression = "${wait}", defaultValue = "true")
    private boolean wait;
    /**
     * Whether or not to write properties used by the plugin to amps.properties.
     */
    @MojoParameter (expression = "${amps.properties}", required = true, defaultValue = "false")
    private boolean writePropertiesToFile;

    protected void doExecute() throws MojoExecutionException, MojoFailureException
    {
        final MavenGoals goals = getMavenGoals();
        ProductHandler product = createProductHandler();
        Product ctx = getProductContexts(goals).get(0);

        int actualHttpPort = product.start(ctx);

        getLog().info(product.getId() + " started successfully and available at http://localhost:" + actualHttpPort + ctx.getContextPath());

        writePropertiesFile(ctx, actualHttpPort);

        if (wait)
        {
            getLog().info("Type CTRL-C to exit");
            try
            {
                while (System.in.read() != CONTROL_C)
                {
                }
            }
            catch (final IOException e)
            {
                // ignore
            }
        }
    }

    private void writePropertiesFile(final Product ctx, final int actualHttpPort) throws MojoExecutionException
    {
        if (writePropertiesToFile)
        {
            final Properties properties = new Properties();
            properties.setProperty("http.port", String.valueOf(actualHttpPort));
            properties.setProperty("context.path", ctx.getContextPath());

            final File ampsProperties = new File(project.getBuild().getDirectory(), "amps.properties");
            OutputStream out = null;
            try
            {
                out = new FileOutputStream(ampsProperties);
                properties.store(out, "");
            }
            catch (IOException e)
            {
                throw new MojoExecutionException("Error writing " + ampsProperties.getAbsolutePath(), e);
            }
            finally
            {
                IOUtils.closeQuietly(out);
            }
        }
    }
}
