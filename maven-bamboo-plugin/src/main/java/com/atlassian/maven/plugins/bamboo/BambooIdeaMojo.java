package com.atlassian.maven.plugins.bamboo;

import com.atlassian.maven.plugins.refapp.cli.IdeaMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

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
