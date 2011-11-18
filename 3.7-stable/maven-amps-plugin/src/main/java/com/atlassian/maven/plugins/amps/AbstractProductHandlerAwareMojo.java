package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.product.ProductHandler;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.plugin.MojoExecutionException;

public abstract class AbstractProductHandlerAwareMojo extends AbstractProductAwareMojo
{
    /**
     * Create a ProductHandler
     * @param productId the product nickname (not the instance id)
     * @return a product handler for this nickname
     * @throws MojoExecutionException
     */
    protected ProductHandler createProductHandler(String productId) throws MojoExecutionException
    {
        return ProductHandlerFactory.create(productId, getMavenContext(), getMavenGoals());
    }
}
