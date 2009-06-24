package com.atlassian.maven.plugins.conf;

import com.atlassian.maven.plugins.amps.CopyBundledDependenciesMojo;
import com.atlassian.maven.plugins.amps.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal copy-bundled-dependencies
 */
public class ConfluenceCopyBundledDependenciesMojo extends CopyBundledDependenciesMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new ConfluenceWebappHandler();
    }
}
