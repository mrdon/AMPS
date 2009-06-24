package com.atlassian.maven.plugins.bamboo;

import com.atlassian.maven.plugins.refapp.pdk.UninstallMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal uninstall
 */
public class BambooUninstallMojo extends UninstallMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new BambooWebappHandler();
    }
}
