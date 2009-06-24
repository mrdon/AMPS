package com.atlassian.maven.plugins.bamboo;

import com.atlassian.maven.plugins.amps.pdk.UninstallMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @extendsPlugin amps
 * @goal uninstall
 */
public class BambooUninstallMojo extends UninstallMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return "bamboo";
    }
}
