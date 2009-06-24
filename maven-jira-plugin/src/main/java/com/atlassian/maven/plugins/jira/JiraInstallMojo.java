package com.atlassian.maven.plugins.jira;

import com.atlassian.maven.plugins.refapp.pdk.InstallMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal install
 */
public class JiraInstallMojo extends InstallMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new JiraWebappHandler();
    }
}
