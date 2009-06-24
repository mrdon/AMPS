package com.atlassian.maven.plugins.jira;

import com.atlassian.maven.plugins.amps.pdk.UninstallMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @extendsPlugin amps
 * @goal uninstall
 */
public class JiraUninstallMojo extends UninstallMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return "jira";
    }
}
