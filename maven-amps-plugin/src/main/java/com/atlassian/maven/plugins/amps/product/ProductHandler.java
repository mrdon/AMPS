package com.atlassian.maven.plugins.amps.product;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;

import com.atlassian.maven.plugins.amps.Product;

public interface ProductHandler
{
    /**
     * @return a convenient string to identify this application, especially for the context path.
     * No further restriction is defined on this ID.
     */
    String getId();

    /**
     * Extracts the product and its home, prepares them and starts the product.
     * @return the port on which the product is accessible
     */
    int start(Product ctx) throws MojoExecutionException;

    void stop(Product ctx) throws MojoExecutionException;

    int getDefaultHttpPort();

    File getHomeDirectory(Product product);

    void createHomeZip(File homeDir, File homeZipFile, String productId) throws MojoExecutionException;
}
