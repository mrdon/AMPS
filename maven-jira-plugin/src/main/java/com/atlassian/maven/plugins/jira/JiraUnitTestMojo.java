package com.atlassian.maven.plugins.jira;

import com.atlassian.maven.plugins.refapp.UnitTestMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal unit-test
 */
public class JiraUnitTestMojo extends UnitTestMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new JiraWebappHandler();
    }
}
