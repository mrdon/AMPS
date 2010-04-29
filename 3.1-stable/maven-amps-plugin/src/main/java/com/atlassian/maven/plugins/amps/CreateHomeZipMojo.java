package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.util.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;

import java.io.File;

/**
 * Creates a zip file containing the previous run's home directory
 * in the proper format to use as test-resources.
 *
 * @since 3.1-m3
 */
@MojoGoal("create-home-zip")
public class CreateHomeZipMojo extends AbstractProductAwareMojo {

    public void execute() throws MojoExecutionException, MojoFailureException {
        final File appDir = FileUtils.file(getMavenContext().getProject().getBuild().getDirectory(),getProductId());
        final File homeDir = new File(appDir, "home");
        final File zipFile = new File(appDir, "generated-test-resources.zip");

        getMavenGoals().createHomeResourcesZip(homeDir, zipFile, getProductId());
    }
}
