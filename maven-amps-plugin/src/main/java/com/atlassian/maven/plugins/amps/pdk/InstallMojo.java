package com.atlassian.maven.plugins.amps.pdk;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.atlassian.maven.plugins.amps.product.ProductHandler;

/**
 * @goal install
 */
public class InstallMojo extends AbstractPdkMojo
{
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        ensurePluginKeyExists();
        final ProductHandler product = createProductHandler();
        getMavenGoals().installPlugin(pluginKey, server, getHttpPort(product), getContextPath(product), username, password);
    }
}
