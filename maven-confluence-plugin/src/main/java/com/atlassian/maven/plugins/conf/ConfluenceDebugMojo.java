package com.atlassian.maven.plugins.conf;

import com.atlassian.maven.plugins.refapp.DebugMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal debug
 * @execute phase="package"
 */
public class ConfluenceDebugMojo extends DebugMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new ConfluenceWebappHandler();
    }
}
