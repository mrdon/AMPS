package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.product.ProductHandler;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.jfrog.maven.annomojo.annotations.MojoComponent;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;
import org.jfrog.maven.annomojo.annotations.MojoRequiresDependencyResolution;

import java.io.File;

/**
 * Run the integration tests against the webapp
 */
@MojoGoal("integration-test")
@MojoRequiresDependencyResolution("test")
public class IntegrationTestMojo extends AbstractProductHandlerMojo
{
    /**
     * Pattern for to use to find integration tests
     */
    @MojoParameter(expression = "${functional.test.pattern}")
    private final String functionalTestPattern = "it/**";

    /**
     * The directory containing generated test classes of the project being tested.
     */
    @MojoParameter(expression = "${project.build.testOutputDirectory}", required = true)
    private File testClassesDirectory;

    /**
     * Whether the reference application will not be started or not
     */
    @MojoParameter(expression = "${no.webapp}", defaultValue = "false")
    private final boolean noWebapp = false;

    @MojoComponent
    private ArtifactHandlerManager artifactHandlerManager;

    @MojoParameter(expression="${maven.test.skip}", defaultValue = "false")
    private boolean testsSkip = false;

    @MojoParameter(expression="${skipTests}", defaultValue = "false")
    private boolean skipTests = false;

    protected void doExecute() throws MojoExecutionException
    {
        final MavenProject project = getMavenContext().getProject();

        // workaround for MNG-1682/MNG-2426: force maven to install artifact using the "jar" handler
        project.getArtifact().setArtifactHandler(artifactHandlerManager.getArtifactHandler("jar"));

        if (!new File(testClassesDirectory, "it").exists())
        {
            getLog().info("No integration tests found");
            return;
        }

        if (skipTests || testsSkip)
        {
            getLog().info("Integration tests skipped");
            return;
        }

        final MavenGoals goals = getMavenGoals();
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

    private void runTestsForProduct(String productId, MavenGoals goals, String pluginJar) throws MojoExecutionException
    {
        ProductHandler product = ProductHandlerFactory.create(productId, getMavenContext().getProject(), goals);
        Product ctx = getProductContexts(goals).get(0);
        ctx.setInstallPlugin(installPlugin);
        
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