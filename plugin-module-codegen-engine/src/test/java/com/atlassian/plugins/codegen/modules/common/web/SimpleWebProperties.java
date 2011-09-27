package com.atlassian.plugins.codegen.modules.common.web;

/**
 * @since 3.6
 */
public class SimpleWebProperties extends AbstractWebFragmentProperties
{
    public SimpleWebProperties()
    {
        this("Simple Web Module");
    }

    public SimpleWebProperties(String moduleName)
    {
        super(moduleName);
    }
}
