package com.atlassian.maven.plugins.bamboo;

import com.atlassian.maven.plugins.refapp.RunMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal run
 * @execute phase="package"
 */
public class BambooRunMojo extends RunMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new BambooWebappHandler();
    }
}
