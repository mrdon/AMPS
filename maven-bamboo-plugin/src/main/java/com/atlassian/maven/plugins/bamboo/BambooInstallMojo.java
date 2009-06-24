package com.atlassian.maven.plugins.bamboo;

import com.atlassian.maven.plugins.amps.pdk.InstallMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @extendsPlugin amps
 * @goal install
 */
public class BambooInstallMojo extends InstallMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return "bamboo";
    }
}
