package com.atlassian.maven.plugins.conf;

import com.atlassian.maven.plugins.refapp.pdk.InstallMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal install
 */
public class ConfluenceInstallMojo extends InstallMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new ConfluenceWebappHandler();
    }
}
