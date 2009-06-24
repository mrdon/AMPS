package com.atlassian.maven.plugins.conf;

import com.atlassian.maven.plugins.amps.DebugMojo;
import com.atlassian.maven.plugins.amps.WebappHandler;

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
