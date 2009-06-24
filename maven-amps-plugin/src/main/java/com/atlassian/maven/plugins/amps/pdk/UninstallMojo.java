package com.atlassian.maven.plugins.amps.pdk;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.MavenContext;
import com.atlassian.maven.plugins.amps.ProductHandler;

/**
 * @goal uninstall
 */
public class UninstallMojo extends AbstractPdkMojo {

    public void execute() throws MojoExecutionException, MojoFailureException {
        MavenGoals goals = new MavenGoals(new MavenContext(project, session, pluginManager, getLog()));

        ensurePluginKeyExists();
        ProductHandler product = createProductHandler(goals);
        goals.uninstallPlugin(pluginKey, server, getHttpPort(product), getContextPath(product));
    }
}
