package com.atlassian.maven.plugins.refapp;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;
import com.atlassian.maven.plugins.amps.CopyBundledDependenciesMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;

/**
 * Copies bundled dependencies into META-INF/lib
 * @extendsPlugin amps
 * @goal copy-bundled-dependencies
 */
public class RefappCopyBundledDependenciesMojo extends CopyBundledDependenciesMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return ProductHandlerFactory.REFAPP;
    }
}
