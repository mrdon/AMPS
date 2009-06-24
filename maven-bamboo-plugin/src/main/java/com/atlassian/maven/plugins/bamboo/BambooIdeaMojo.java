package com.atlassian.maven.plugins.bamboo;

import com.atlassian.maven.plugins.amps.cli.IdeaMojo;
import com.atlassian.maven.plugins.amps.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal idea
 */
public class BambooIdeaMojo extends IdeaMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new BambooWebappHandler();
    }
}
