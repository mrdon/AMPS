package com.atlassian.maven.plugins.jira;

import com.atlassian.maven.plugins.amps.CopyBundledDependenciesMojo;
import com.atlassian.maven.plugins.amps.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal copy-bundled-dependencies
 */
public class JiraCopyBundledDependenciesMojo extends CopyBundledDependenciesMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new JiraWebappHandler();
    }
}
