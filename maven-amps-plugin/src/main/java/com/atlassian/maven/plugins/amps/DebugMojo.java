package com.atlassian.maven.plugins.amps;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jfrog.maven.annomojo.annotations.MojoExecute;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;
import org.jfrog.maven.annomojo.annotations.MojoRequiresDependencyResolution;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import com.atlassian.maven.plugins.amps.util.GoogleAmpsTracker;

import java.util.List;

/**
 * Debug the webapp
 */
@MojoGoal ("debug")
@MojoExecute (phase = "package")
@MojoRequiresDependencyResolution
public class DebugMojo extends RunMojo
{
    private static final String DEFAULT_JVM_ARGS = "-Xmx512m -XX:MaxPermSize=160m";

    /**
     * port for debugging
     */
    @MojoParameter (expression = "${jvm.debug.port}", defaultValue = "5005")
    protected int jvmDebugPort;

    /**
     * Suspend when debugging
     */
    @MojoParameter (expression = "${jvm.debug.suspend}")
    protected boolean jvmDebugSuspend = false;


    @Override
    protected void doExecute() throws MojoExecutionException, MojoFailureException
    {
        getGoogleTracker().track(GoogleAmpsTracker.DEBUG);

        final List<ProductExecution> productExecutions = getProductExecutions();

        if (jvmArgs == null)
        {
            jvmArgs = DEFAULT_JVM_ARGS;
        }

        int counter = 0;
        for (ProductExecution productExecution : productExecutions)
        {
            final Product product = productExecution.getProduct();

            if (product.getJvmDebugPort() == 0)
            {
                product.setJvmDebugPort(jvmDebugPort + counter++);
            }
            final int port = product.getJvmDebugPort();

            String debugArgs = " -Xdebug -Xrunjdwp:transport=dt_socket,address=" +
                               String.valueOf(port) + ",suspend=" + (jvmDebugSuspend ? "y" : "n") + ",server=y ";

            if (product.getJvmArgs() == null)
            {
                product.setJvmArgs(jvmArgs);
            }

            product.setJvmArgs(product.getJvmArgs() + debugArgs);

            if (writePropertiesToFile)
            {
                if (productExecutions.size() == 1)
                {
                    properties.put("debug.port", String.valueOf(port));
                }

                properties.put("debug." + product.getInstanceId() + ".port", String.valueOf(port));
            }

            if (ProductHandlerFactory.FECRU.equals(getDefaultProductId()) && debugNotSet()) {
                String message = "You must set the ATLAS_OPTS environment variable to the following string:'" + product.getJvmArgs() + "' when calling atlas-debug to enable Fisheye/Crucible debugging.";
                getLog().error(message);
                throw new MojoFailureException(message);
            }
        }

        startProducts(productExecutions);
    }

    private boolean debugNotSet()
    {
        String atlasOpts = System.getenv("ATLAS_OPTS");
        return atlasOpts == null || !atlasOpts.contains("-Xdebug");
    }
}
