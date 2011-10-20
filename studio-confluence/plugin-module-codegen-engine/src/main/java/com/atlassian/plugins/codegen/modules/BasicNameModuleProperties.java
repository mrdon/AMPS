package com.atlassian.plugins.codegen.modules;

/**
 * @since 3.6
 */
public class BasicNameModuleProperties extends AbstractNameBasedModuleProperties
{

    public BasicNameModuleProperties()
    {
        this("MyPluginModule");
    }

    public BasicNameModuleProperties(String moduleName)
    {
        super();

        setModuleNameAndKey(moduleName);
    }


}