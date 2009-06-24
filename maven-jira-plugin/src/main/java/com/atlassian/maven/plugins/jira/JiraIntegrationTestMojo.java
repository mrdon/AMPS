package com.atlassian.maven.plugins.jira;

import com.atlassian.maven.plugins.refapp.IntegrationTestMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal integration-test
 */
public class JiraIntegrationTestMojo extends IntegrationTestMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new JiraWebappHandler();
    }
}
