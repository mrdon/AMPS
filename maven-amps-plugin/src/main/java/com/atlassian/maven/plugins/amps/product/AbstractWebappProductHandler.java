package com.atlassian.maven.plugins.amps.product;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;

public abstract class AbstractWebappProductHandler extends AbstractProductHandler
{
    public AbstractWebappProductHandler(final MavenProject project, final MavenGoals goals, Log log, PluginProvider pluginProvider)
    {
        super(project, goals, pluginProvider);
    }

    public final void stop(final Product ctx) throws MojoExecutionException
    {
        goals.stopWebapp(ctx.getInstanceId(), ctx.getContainerId());
    }

    @Override
    protected final File extractApplication(Product ctx, File homeDir) throws MojoExecutionException
    {
        // Copy the webapp war to target
        return goals.copyWebappWar(ctx.getId(), getBaseDirectory(ctx),
                new ProductArtifact(getArtifact().getGroupId(), getArtifact().getArtifactId(), ctx.getVersion()));
    }

    @Override
    protected final int startApplication(Product ctx, File app, File homeDir, Map<String, String> properties) throws MojoExecutionException
    {
        return goals.startWebapp(ctx.getInstanceId(), app, properties, getExtraContainerDependencies(), ctx);
    }

    @Override
    protected boolean supportsStaticPlugins()
    {
        return true;
    }
    
    @Override
    protected String getLog4jPropertiesPath()
    {
        return "WEB-INF/classes/log4j.properties";
    }
    
    protected abstract List<ProductArtifact> getExtraContainerDependencies();
    protected abstract ProductArtifact getArtifact();
}
