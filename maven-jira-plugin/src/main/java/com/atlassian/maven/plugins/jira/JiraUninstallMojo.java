package com.atlassian.maven.plugins.jira;

import com.atlassian.maven.plugins.refapp.pdk.UninstallMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal uninstall
 */
public class JiraUninstallMojo extends UninstallMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new JiraWebappHandler();
    }
}
