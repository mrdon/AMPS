package com.atlassian.maven.plugins.bamboo;

import com.atlassian.maven.plugins.refapp.IntegrationTestMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal integration-test
 */
public class BambooIntegrationTestMojo extends IntegrationTestMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new BambooWebappHandler();
    }
}
