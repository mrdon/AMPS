package com.atlassian.maven.plugins.refapp.pdk;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.atlassian.maven.plugins.refapp.MavenGoals;
import com.atlassian.maven.plugins.refapp.MavenContext;

/**
 * @goal uninstall
 */
public class UninstallMojo extends AbstractPdkMojo {

    public void execute() throws MojoExecutionException, MojoFailureException {
        MavenGoals goals = new MavenGoals(new MavenContext(project, session, pluginManager, getLog()), getWebappHandler());

        ensurePluginKeyExists();
        goals.uninstallPlugin(pluginKey, getHttpPort());
    }
}
