package com.atlassian.maven.plugins.jira;

import com.atlassian.maven.plugins.amps.CopyBundledDependenciesMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @extendsPlugin amps
 * @goal copy-bundled-dependencies
 */
public class JiraCopyBundledDependenciesMojo extends CopyBundledDependenciesMojo
{
    @Override
    protected String getProductId() throws MojoExecutionException
    {
        return "jira";
    }
}
