package com.atlassian.maven.plugins.bamboo;

import com.atlassian.maven.plugins.refapp.UnitTestMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal unit-test
 */
public class BambooUnitTestMojo extends UnitTestMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new BambooWebappHandler();
    }
}
