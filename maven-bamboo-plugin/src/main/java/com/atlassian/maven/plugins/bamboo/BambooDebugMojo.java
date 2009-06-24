package com.atlassian.maven.plugins.bamboo;

import com.atlassian.maven.plugins.amps.DebugMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @extendsPlugin amps
 * @goal debug
 * @execute phase="package"
 */
public class BambooDebugMojo extends DebugMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return "bamboo";
    }
}
