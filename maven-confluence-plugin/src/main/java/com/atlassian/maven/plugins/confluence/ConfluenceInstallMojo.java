package com.atlassian.maven.plugins.confluence;

import com.atlassian.maven.plugins.amps.pdk.InstallMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @extendsPlugin amps
 * @goal install
 */
public class ConfluenceInstallMojo extends InstallMojo
{
    @Override
    protected String getProductId() throws MojoExecutionException
    {
        return "confluence";
    }
}
