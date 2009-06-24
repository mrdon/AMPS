package com.atlassian.maven.plugins.conf;

import com.atlassian.maven.plugins.refapp.IntegrationTestMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal integration-test
 */
public class ConfluenceIntegrationTestMojo extends IntegrationTestMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new ConfluenceWebappHandler();
    }
}
