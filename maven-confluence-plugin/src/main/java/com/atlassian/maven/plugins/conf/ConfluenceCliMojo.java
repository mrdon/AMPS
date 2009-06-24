package com.atlassian.maven.plugins.conf;

import com.atlassian.maven.plugins.refapp.cli.CliMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal cli
 */
public class ConfluenceCliMojo extends CliMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new ConfluenceWebappHandler();
    }
}
