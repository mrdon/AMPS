package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.product.ProductHandler;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import com.atlassian.maven.plugins.amps.product.studio.StudioProductHandler;
import com.atlassian.maven.plugins.amps.util.GoogleAmpsTracker;

import com.google.common.collect.Lists;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Creates a zip file containing the previous run's home directory
 * in the proper format to use as test-resources.
 *
 * @since 3.1-m3
 */
@MojoGoal("create-home-zip")
public class CreateHomeZipMojo extends AbstractProductHandlerMojo {

    /**
     * Generated home directory zip file.
     */
    @MojoParameter(expression = "${homeZip}", required = false)
    protected File homeZip;

    public void doExecute() throws MojoExecutionException, MojoFailureException
    {
        Product product = getProduct(instanceId, getProductId());
        ProductHandler productHandler = createProductHandler(product.getId());

        if (ProductHandlerFactory.STUDIO.equals(product.getId()))
        {
            configureStudio(product, (StudioProductHandler) productHandler);
        }

        final File snapshotDir = productHandler.getSnapshotDirectory(product);
        if (homeZip == null)
        {
            homeZip = new File(productHandler.getBaseDirectory(product), "generated-test-resources.zip");
        }

        productHandler.createHomeZip(snapshotDir, homeZip, product);

        getLog().info("Home directory zip created successfully at " + homeZip.getAbsolutePath());

        // Make the file the artifact of the project
        getMavenGoals().attachArtifact(homeZip, "zip");

    }




    /**
     * Configure the Studio product.
     *
     * @param studioProduct the studio product. Must not be another product, neither null.
     * @param studioProductHandler the Studio product handler
     */
    private void configureStudio(Product studioProduct, StudioProductHandler studioProductHandler) throws MojoExecutionException
    {
        List<ProductExecution> executions = Lists.newArrayList(new ProductExecution(studioProduct, studioProductHandler));
        includeStudioDependentProducts(executions, getMavenGoals());
    }

    /**
     * Returns the product to snapshot.
     * @param instanceId the instance to snapshot (preferred solution to reference the product)
     * @param productId the product to snapshot if instanceId is null. It is not advisable to use this parameter,
     * as it doesn't reference the instance in a unique manner.
     *
     * @return a Product object
     * @throws MojoExecutionException
     */
    private Product getProduct(final String instanceId, final String productId) throws MojoExecutionException
    {
        Map<String, Product> contexts = getProductContexts(getMavenGoals());

        Product product = null;
        if (instanceId != null)
        {
            product = contexts.get(instanceId);
            if (product == null)
            {
                throw new MojoExecutionException("There is no instance with name " + instanceId + " defined in the pom.xml");
            }
        }
        else
        {
            for (Product candidate : contexts.values())
            {
                if (candidate.getId().equals(productId))
                {
                    product = candidate;
                    break;
                }
            }
            if (product == null)
            {
                throw new MojoExecutionException("There is no product with name " + productId + " defined in the pom.xml. Please use -DinstanceId=..." +
                		" to set the instance to snapshot.");
            }
        }

        return product;
    }
}
