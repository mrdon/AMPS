package com.atlassian.maven.plugins.amps;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import com.atlassian.maven.plugins.amps.product.ProductHandler;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;

/**
 * Run the integration tests against the webapp
 *
 * @requiresDependencyResolution integration-test
 * @goal integration-test
 */
public class IntegrationTestMojo
        extends AbstractProductMojo
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
        MavenGoals goals = new MavenGoals(new MavenContext(project, session, pluginManager, getLog()));

        final String pluginJar = targetDirectory.getAbsolutePath() + "/" + finalName + ".jar";

        runTestsForProduct(getProductId(), goals, pluginJar);

        for (String productId : ProductHandlerFactory.getIds())
        {
            if (containsTests(productId) && !productId.equals(getProductId()))
            {
                runTestsForProduct(productId, goals, pluginJar);
            }
        }

    }

    private void runTestsForProduct(String productId, MavenGoals goals, String pluginJar)
            throws MojoExecutionException
    {
        ProductHandler product = ProductHandlerFactory.create(productId, project, goals);
        ProductContext ctx = createProductContext(product);
        int actualHttpPort;
        if (!noWebapp)
        {
            actualHttpPort = product.start(ctx);

        }
        goals.runTests(getProductId(), containerId, functionalTestPattern, actualHttpPort, ctx.getContextPath(), pluginJar);

        if (!noWebapp)
        {
            product.stop(ctx);
        }
    }

    private boolean containsTests(String type)
    {
        return scanFile(new File(testClassesDirectory, "it"), type);
    }

    private boolean scanFile(File file, String type)
    {
        if (file.isDirectory())
        {
            if (file.getName().equals(type))
            {
                return true;
            }
            else
            {
                for (File kid : file.listFiles())
                {
                    if (scanFile(kid, type))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}