package com.atlassian.maven.plugins.jira;

import com.atlassian.maven.plugins.amps.cli.IdeaMojo;
import com.atlassian.maven.plugins.amps.WebappHandler;

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
