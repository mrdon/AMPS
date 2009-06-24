package com.atlassian.maven.plugins.conf;

import com.atlassian.maven.plugins.amps.cli.IdeaMojo;
import com.atlassian.maven.plugins.amps.WebappHandler;

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
