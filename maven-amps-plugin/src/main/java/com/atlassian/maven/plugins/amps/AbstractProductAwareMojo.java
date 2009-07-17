package com.atlassian.maven.plugins.amps;

import org.apache.maven.plugin.MojoExecutionException;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

public abstract class AbstractProductAwareMojo extends AbstractAmpsMojo
{
    /**
     * Product id
     */
    @MojoParameter(expression = "${product}")
    private String product;

    protected String getDefaultProductId() throws MojoExecutionException
    {
        return null;
    }

    protected String getProductId() throws MojoExecutionException
    {
        if (product == null)
        {
            product = getDefaultProductId();
            if (product == null)
            {
                throw new MojoExecutionException("The product must be specified");
            }
        }
        return product;
    }
}
