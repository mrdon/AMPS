package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.product.ProductHandler;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.plugin.MojoExecutionException;

public abstract class AbstractProductHandlerAwareMojo extends AbstractProductAwareMojo
{
    protected ProductHandler createProductHandler() throws MojoExecutionException
    {
        return ProductHandlerFactory.create(getProductId(), getMavenContext().getProject(), getMavenGoals());
    }
}
