package com.atlassian.maven.plugins.amps;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.model.Plugin;
import org.jfrog.maven.annomojo.annotations.MojoParameter;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import com.atlassian.maven.plugins.amps.product.RefappProductHandler;

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

    protected final String getProductId() throws MojoExecutionException
    {
        if (product == null)
        {
            product = getDefaultProductId();
            if (product == null && project != null)
            {
                for (Plugin plugin : (List<Plugin>)project.getBuild().getPlugins())
                {
                    if ("com.atlassian.maven.plugins".equals(plugin.getGroupId()))
                    {
                        for (String productKey : ProductHandlerFactory.getIds())
                        {
                            if (("maven-"+productKey+"-plugin").equals(plugin.getArtifactId()))
                            {
                                product = productKey;
                                break;
                            }
                        }

                    }
                }
            }
            if (product == null)
            {
                product = ProductHandlerFactory.REFAPP;
            }
        }
        return product;
    }
}
