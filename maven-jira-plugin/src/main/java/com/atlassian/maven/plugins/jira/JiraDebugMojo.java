package com.atlassian.maven.plugins.jira;

import com.atlassian.maven.plugins.refapp.DebugMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal debug
 * @execute phase="package"
 */
public class JiraDebugMojo extends DebugMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new JiraWebappHandler();
    }
}
