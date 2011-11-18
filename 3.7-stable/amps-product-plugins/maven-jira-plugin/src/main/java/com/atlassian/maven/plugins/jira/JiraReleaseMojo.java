package com.atlassian.maven.plugins.jira;

import com.atlassian.maven.plugins.amps.ReleaseMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;

import org.apache.maven.plugin.MojoExecutionException;

public class JiraReleaseMojo extends ReleaseMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return ProductHandlerFactory.JIRA;
    }
}
