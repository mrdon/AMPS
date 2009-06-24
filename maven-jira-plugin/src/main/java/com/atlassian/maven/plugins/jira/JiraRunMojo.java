package com.atlassian.maven.plugins.jira;

import com.atlassian.maven.plugins.refapp.RunMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal run
 * @execute phase="package"
 */
public class JiraRunMojo extends RunMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new JiraWebappHandler();
    }
}
