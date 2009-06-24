package com.atlassian.maven.plugins.conf;

import com.atlassian.maven.plugins.refapp.pdk.UninstallMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal uninstall
 */
public class ConfluenceUninstallMojo extends UninstallMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new ConfluenceWebappHandler();
    }
}
