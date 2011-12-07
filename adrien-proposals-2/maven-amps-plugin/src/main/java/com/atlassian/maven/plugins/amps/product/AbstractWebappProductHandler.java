package com.atlassian.maven.plugins.amps.product;

import java.io.File;

import java.util.List;
import java.util.Map;

import com.atlassian.maven.plugins.amps.MavenContext;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;

import static com.atlassian.maven.plugins.amps.util.ProjectUtils.firstNotNull;
import static com.atlassian.maven.plugins.amps.util.ProjectUtils.createDirectory;

public abstract class AbstractWebappProductHandler extends AbstractProductHandler
{
    public AbstractWebappProductHandler(final MavenContext context, final MavenGoals goals, PluginProvider pluginProvider)
    {
        super(context, goals, pluginProvider);
    }

    public final void stop(final Product ctx) throws MojoExecutionException
    {
        goals.stopWebapp(ctx.getInstanceId(), ctx.getContainerId(), ctx);
    }

    @Override
    protected final File extractApplication(Product ctx, File homeDir) throws MojoExecutionException
    {
        ProductArtifact defaults = getArtifact();
        ProductArtifact artifact = new ProductArtifact(
            firstNotNull(ctx.getGroupId(), defaults.getGroupId()),
            firstNotNull(ctx.getArtifactId(), defaults.getArtifactId()),
            firstNotNull(ctx.getVersion(), defaults.getVersion()));

        // Copy the webapp war to target
        return goals.copyWebappWar(ctx.getId(), getBaseDirectory(ctx), artifact);
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
}
