package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.product.RefappProductHandler;
import com.atlassian.maven.plugins.amps.product.ProductHandler;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

public abstract class AbstractAmpsMojo extends AbstractMojo
{
    /**
     * Product id
     *
     * @parameter expression="${product}
     */
    private String product;

    /**
     * The Maven Project Object
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;


    protected ProductHandler createProductHandler(MavenGoals goals) throws MojoExecutionException
    {
        return ProductHandlerFactory.create(getProductId(), project, goals);
    }

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
