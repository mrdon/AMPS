package com.atlassian.maven.plugins.amps;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

/**
 * Compresses JavaScript resources with the YUI compressor Maven plugin.  Creates compressed versions of all
 * JavaScript resources by attaching the '-min' suffix.
 *
 * @since 3.2
 */
@MojoGoal("compress-resources")
public class CompressResourcesMojo extends AbstractAmpsMojo
{
    /**
     * Whether to compress the resources or not.  Defaults to true.
     */
    @MojoParameter(defaultValue = "true")
    private boolean compressResources;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if (compressResources)
        {
            getMavenGoals().compressResources();
        }
        else
        {
            getLog().debug("Compressing resources disabled");
        }
    }
}
