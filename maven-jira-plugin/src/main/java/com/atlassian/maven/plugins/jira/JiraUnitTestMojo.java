package com.atlassian.maven.plugins.jira;

import com.atlassian.maven.plugins.amps.UnitTestMojo;
import com.atlassian.maven.plugins.amps.WebappHandler;

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
