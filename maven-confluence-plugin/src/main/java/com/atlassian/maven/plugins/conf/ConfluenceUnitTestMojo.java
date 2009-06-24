package com.atlassian.maven.plugins.conf;

import com.atlassian.maven.plugins.refapp.UnitTestMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal unit-test
 */
public class ConfluenceUnitTestMojo extends UnitTestMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new ConfluenceWebappHandler();
    }
}
