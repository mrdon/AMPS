package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.plugin.MojoExecutionException;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

public abstract class AbstractProductAwareMojo extends AbstractAmpsMojo
{
    /**
     * Product id
     */
    @MojoParameter(expression = "${product}")
    private String product;

    /**
     * Instance to run. If provided, used to determine the product to use, instead of
     * using the product ID.
     */
    @MojoParameter(expression = "${instanceId}")
    protected String instanceId;


    protected String getDefaultProductId() throws MojoExecutionException
    {
        return null;
    }

    protected final String getProductId() throws MojoExecutionException
    {
        if (product == null)
        {
            product = getDefaultProductId();
            if (product == null && ProductHandlerFactory.getIds().contains(getPluginInformation().getId()))
            {
                product = getPluginInformation().getId();
            }
            else if (product == null)
            {
                product = ProductHandlerFactory.REFAPP;
            }
        }
        return product;
    }
}
