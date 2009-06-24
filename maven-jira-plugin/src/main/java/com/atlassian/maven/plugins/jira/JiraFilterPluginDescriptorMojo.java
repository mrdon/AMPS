package com.atlassian.maven.plugins.jira;

import com.atlassian.maven.plugins.refapp.FilterPluginDescriptorMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal filter-plugin-descriptor
 */
public class JiraFilterPluginDescriptorMojo extends FilterPluginDescriptorMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new JiraWebappHandler();
    }
}
