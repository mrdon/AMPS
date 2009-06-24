package com.atlassian.maven.plugins.jira;

import com.atlassian.maven.plugins.refapp.cli.IdeaMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal idea
 */
public class JiraIdeaMojo extends IdeaMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new JiraWebappHandler();
    }
}
