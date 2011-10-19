package com.atlassian.maven.plugins.amps;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import java.util.List;

/**
 * Stop the webapps started by RunMojo.
 * This may be useful when you use -Dwait=false for the RunMojo and you want
 * the products to make a clean shutdown.
 */
@MojoGoal ("stop")
public class StopMojo extends RunMojo
{
    protected void doExecute() throws MojoExecutionException, MojoFailureException
    {
        final List<ProductExecution> productExecutions = getProductExecutions();
        setParallelMode(productExecutions);
        stopProducts(productExecutions);
    }

}
