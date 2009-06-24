package com.atlassian.maven.plugins.bamboo;

import com.atlassian.maven.plugins.refapp.pdk.InstallMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal install
 */
public class BambooInstallMojo extends InstallMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new BambooWebappHandler();
    }
}
