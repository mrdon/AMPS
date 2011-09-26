package com.atlassian.plugins.codegen.modules;

/**
 * @since 3.5
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