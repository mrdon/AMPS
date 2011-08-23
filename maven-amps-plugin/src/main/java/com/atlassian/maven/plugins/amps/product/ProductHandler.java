package com.atlassian.maven.plugins.amps.product;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;

import com.atlassian.maven.plugins.amps.Product;

public interface ProductHandler
{
    /**
     * @return the webapp id
     */
    String getId();

    int start(Product ctx) throws MojoExecutionException;

    void stop(Product ctx) throws MojoExecutionException;

    int getDefaultHttpPort();

    File getHomeDirectory(Product product);

    void createHomeZip(File homeDir, File homeZipFile, String productId) throws MojoExecutionException;
}
