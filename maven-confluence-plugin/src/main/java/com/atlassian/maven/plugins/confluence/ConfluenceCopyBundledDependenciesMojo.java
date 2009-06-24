package com.atlassian.maven.plugins.confluence;

import com.atlassian.maven.plugins.amps.CopyBundledDependenciesMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @extendsPlugin amps
 * @goal copy-bundled-dependencies
 */
public class ConfluenceCopyBundledDependenciesMojo extends CopyBundledDependenciesMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return "confluence";
    }
}
