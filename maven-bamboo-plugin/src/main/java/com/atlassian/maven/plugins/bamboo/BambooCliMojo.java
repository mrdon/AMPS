package com.atlassian.maven.plugins.bamboo;

import com.atlassian.maven.plugins.refapp.cli.CliMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal cli
 */
public class BambooCliMojo extends CliMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new BambooWebappHandler();
    }
}
