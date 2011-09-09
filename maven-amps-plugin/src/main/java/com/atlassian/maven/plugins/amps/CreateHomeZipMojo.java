package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.product.ProductHandler;
import com.atlassian.maven.plugins.amps.util.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

import java.io.File;
import java.util.Collections;

/**
 * Creates a zip file containing the previous run's home directory
 * in the proper format to use as test-resources.
 *
 * @since 3.1-m3
 */
@MojoGoal("create-home-zip")
public class CreateHomeZipMojo extends AbstractProductHandlerAwareMojo {

    /**
     * Generated home directory zip file.
     */
    @MojoParameter(expression = "${homeZip}", required = false)
    protected File homeZipFile;

    public void execute() throws MojoExecutionException, MojoFailureException {
        ProductHandler productHandler = createProductHandler(getProductId());

        final File appDir = FileUtils.file(getMavenContext().getProject().getBuild().getDirectory(),getProductId());
        final File homeDir = new File(appDir, "home");
        if (homeZipFile == null)
        {
            homeZipFile = new File(appDir, "generated-test-resources.zip");
        }

        productHandler.createHomeZip(homeDir, homeZipFile, getProductId());

        getLog().info("Home directory zip created successfully at " + homeZipFile.getAbsolutePath());
    }
}
