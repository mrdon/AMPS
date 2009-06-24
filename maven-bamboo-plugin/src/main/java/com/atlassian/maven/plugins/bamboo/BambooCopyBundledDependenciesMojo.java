package com.atlassian.maven.plugins.bamboo;

import com.atlassian.maven.plugins.refapp.CopyBundledDependenciesMojo;
import com.atlassian.maven.plugins.refapp.WebappHandler;

/**
 * @extendsPlugin refapp
 * @goal copy-bundled-dependencies
 */
public class BambooCopyBundledDependenciesMojo extends CopyBundledDependenciesMojo
{
    @Override
    protected WebappHandler getWebappHandler()
    {
        return new BambooWebappHandler();
    }
}
