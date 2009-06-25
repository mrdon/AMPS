package com.atlassian.maven.plugins.jira;

import com.atlassian.maven.plugins.amps.pdk.UninstallMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
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
        return ProductHandlerFactory.JIRA;
    }
}
