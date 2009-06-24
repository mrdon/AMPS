package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.product.RefappProductHandler;
import com.atlassian.maven.plugins.amps.product.ProductHandler;
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
        String id = getProductId();

        if ("refapp".equals(id))
        {
            return new RefappProductHandler(project, goals);
        }

        throw new MojoExecutionException("Unknown product id:" + id);

    }

    protected String getProductId() throws MojoExecutionException
    {
        if (product == null)
        {
            throw new MojoExecutionException("The product must be specified");
        }
        return product;
    }
}
