package com.atlassian.maven.plugins.conf;

import com.atlassian.maven.plugins.refapp.FilterPluginDescriptorMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal filter-plugin-descriptor
 */
public class ConfluenceFilterPluginDescriptorMojo extends FilterPluginDescriptorMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new ConfluenceWebappHandler();
    }
}
