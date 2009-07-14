package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.product.RefappProductHandler;
import com.atlassian.maven.plugins.amps.product.ProductHandler;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;
import org.apache.maven.execution.MavenSession;

public abstract class AbstractAmpsMojo extends AbstractMojo
{
    /**
     * The Maven Project Object
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * The Maven Session Object
     * @parameter expression="${session}"
     * @required
     * @readonly
     */
    private MavenSession session;
    /**
     * The Maven PluginManager Object
     * @component
     * @required
     */
    private PluginManager pluginManager;

    private MavenContext mavenContext;
    private MavenGoals mavenGoals;

    protected MavenContext getMavenContext()
    {
        if (mavenContext == null)
        {
            mavenContext = new MavenContext(project, session, pluginManager, getLog());
        }
        return mavenContext;
    }

    protected MavenGoals getMavenGoals()
    {
        if (mavenGoals == null)
        {
            mavenGoals = new MavenGoals(getMavenContext());
        }
        return mavenGoals;
    }
}
