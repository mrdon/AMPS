package com.atlassian.maven.plugins.amps.product;

import org.apache.maven.project.MavenProject;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.Map;
import java.util.Collection;
import java.util.List;
import java.io.File;

import com.atlassian.maven.plugins.amps.ProductContext;

public interface ProductHandler
{
    /**
     * @return the webapp id
     */
    String getId();

    int start(ProductContext ctx) throws MojoExecutionException;

    void stop(ProductContext ctx) throws MojoExecutionException;

    int getDefaultHttpPort();

}
