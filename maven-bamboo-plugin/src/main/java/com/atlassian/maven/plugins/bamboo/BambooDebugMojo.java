package com.atlassian.maven.plugins.bamboo;

import com.atlassian.maven.plugins.refapp.DebugMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal debug
 * @execute phase="package"
 */
public class BambooDebugMojo extends DebugMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new BambooWebappHandler();
    }
}
