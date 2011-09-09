package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.product.ProductHandler;
import com.google.inject.internal.util.Lists;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.artifact.Artifact;
import org.jfrog.maven.annomojo.annotations.MojoExecute;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;
import org.jfrog.maven.annomojo.annotations.MojoRequiresDependencyResolution;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * Run the webapp
 */
@MojoGoal ("run")
@MojoExecute (phase = "package")
@MojoRequiresDependencyResolution
public class RunMojo extends AbstractTestGroupsHandlerMojo
{
    private static final char CONTROL_C = (char) 27;

    @MojoParameter (expression = "${wait}", defaultValue = "true")
    private boolean wait;

    /**
     * Whether or not to write properties used by the plugin to amps.properties.
     */
    @MojoParameter (expression = "${amps.properties}", required = true, defaultValue = "false")
    protected boolean writePropertiesToFile;

    /**
     * Instance id to run.  If provided, used to determine the product to run instead of just the product ID.
     */
    @MojoParameter(expression = "${instanceId}")
    protected String instanceId;

    /**
     * Test group to run.  If provided, used to determine the products to run.
     */
    @MojoParameter(expression = "${testGroup}")
    protected String testGroup;
    
    /**
     * The properties actually used by the mojo when running
     */
    protected final Map<String, String> properties = new HashMap<String, String>();

    protected void doExecute() throws MojoExecutionException, MojoFailureException
    {
        final List<ProductExecution> productExecutions = getProductExecutions();

        startProducts(productExecutions);
    }

    protected void startProducts(List<ProductExecution> productExecutions) throws MojoExecutionException
    {
        List<String> successMessages = Lists.newArrayList();
        for (ProductExecution productExecution : productExecutions)
        {
            final ProductHandler productHandler = productExecution.getProductHandler();
            final Product product = productExecution.getProduct();
            if (product.isInstallPlugin() == null)
            {
                product.setInstallPlugin(shouldInstallPlugin());
            }

            int actualHttpPort = productHandler.start(product);

            String successMessage = product.getInstanceId() + " started successfully";
            if (actualHttpPort != 0)
            {
                successMessage += " and available at http://localhost:" + actualHttpPort + product.getContextPath();
            }
            getLog().info(successMessage);

            successMessages.add(successMessage);

            if (writePropertiesToFile)
            {
                if (productExecutions.size() == 1)
                {
                    properties.put("http.port", String.valueOf(actualHttpPort));
                    properties.put("context.path", product.getContextPath());
                }

                properties.put("http." + product.getInstanceId() + ".port", String.valueOf(actualHttpPort));
                properties.put("context." + product.getInstanceId() + ".path", product.getContextPath());
            }
        }

        if (writePropertiesToFile)
        {
            writePropertiesFile();
        }
        
        // Repeat the messages at the end, because we're developer-friendly
        if (successMessages.size() > 1)
        {
            getLog().info("Summary:");
            for (String message : successMessages)
            {
                getLog().info(message);
            }
        }

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

    protected List<ProductExecution> getProductExecutions() throws MojoExecutionException
    {
        final List<ProductExecution> productExecutions;
        final MavenGoals goals = getMavenGoals();
        if (!isBlank(testGroup))
        {
            productExecutions = getTestGroupProductExecutions(testGroup);
        }
        else if (!isBlank(instanceId))
        {
            Product ctx = getProductContexts(goals).get(instanceId);
            if (ctx == null)
            {
                throw new MojoExecutionException("No product with instance ID '" + instanceId + "'");
            }
            ProductHandler product = createProductHandler(ctx.getId());
            productExecutions = Collections.singletonList(new ProductExecution(ctx, product));
        }
        else
        {
            Product ctx = getProductContexts(goals).get(getProductId());
            ProductHandler product = createProductHandler(ctx.getId());
            productExecutions = Collections.singletonList(new ProductExecution(ctx, product));
        }
        return includeStudioDependentProducts(productExecutions, goals);
    }

    /**
     * Only install a plugin if the installPlugin flag is true and the project is a jar.  If the test plugin was built,
     * it will be installed as well.
     */
    private boolean shouldInstallPlugin()
    {
        Artifact artifact = getMavenContext().getProject().getArtifact();
        return installPlugin &&
                (artifact != null && !"pom".equalsIgnoreCase(artifact.getType()));
    }

    private void writePropertiesFile() throws MojoExecutionException
    {
        final Properties props = new Properties();

        for (Map.Entry<String, String> entry : properties.entrySet())
        {
            props.setProperty(entry.getKey(), entry.getValue());
        }

        final File ampsProperties = new File(getMavenContext().getProject().getBuild().getDirectory(), "amps.properties");
        OutputStream out = null;
        try
        {
            out = new FileOutputStream(ampsProperties);
            props.store(out, "");
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
