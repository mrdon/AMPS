package com.atlassian.maven.plugins.conf;

import com.atlassian.maven.plugins.amps.UnitTestMojo;
import com.atlassian.maven.plugins.amps.WebappHandler;

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
