package com.atlassian.maven.plugins.conf;

import com.atlassian.maven.plugins.refapp.cli.IdeaMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal idea
 */
public class ConfluenceIdeaMojo extends IdeaMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new ConfluenceWebappHandler();
    }
}
