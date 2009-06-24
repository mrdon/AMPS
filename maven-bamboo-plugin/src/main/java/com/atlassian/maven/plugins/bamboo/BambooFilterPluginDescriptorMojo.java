package com.atlassian.maven.plugins.bamboo;

import com.atlassian.maven.plugins.refapp.FilterPluginDescriptorMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal filter-plugin-descriptor
 */
public class BambooFilterPluginDescriptorMojo extends FilterPluginDescriptorMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new BambooWebappHandler();
    }
}
