package com.atlassian.maven.plugins.amps.pdk;

import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.product.ProductHandler;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;

@MojoGoal("uninstall")
public class UninstallMojo extends AbstractPdkMojo
{
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        ensurePluginKeyExists();
        final MavenGoals goals = getMavenGoals();
        ProductHandler product = createProductHandler();
        goals.uninstallPlugin(pluginKey, server, getHttpPort(product), getContextPath(product));
    }
}
