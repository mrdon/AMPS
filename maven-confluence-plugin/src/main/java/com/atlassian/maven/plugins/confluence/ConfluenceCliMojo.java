package com.atlassian.maven.plugins.confluence;

import com.atlassian.maven.plugins.amps.cli.CliMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @extendsPlugin amps
 * @goal cli
 */
public class ConfluenceCliMojo extends CliMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return "confluence";
    }
}
