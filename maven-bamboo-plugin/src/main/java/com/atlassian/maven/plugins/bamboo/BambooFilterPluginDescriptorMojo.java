package com.atlassian.maven.plugins.bamboo;

import com.atlassian.maven.plugins.amps.FilterPluginDescriptorMojo;
import com.atlassian.maven.plugins.amps.WebappHandler;

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
