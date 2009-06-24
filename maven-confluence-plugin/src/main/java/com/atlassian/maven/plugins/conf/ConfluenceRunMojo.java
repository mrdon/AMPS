package com.atlassian.maven.plugins.conf;

import com.atlassian.maven.plugins.refapp.RunMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal run
 * @execute phase="package"
 */
public class ConfluenceRunMojo extends RunMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new ConfluenceWebappHandler();
    }
}
