package com.atlassian.plugins.codegen.modules.common.moduletype;

import com.atlassian.plugins.codegen.modules.ClassWithInterfaceProperties;

/**
 * @since 3.5
 */
public class ModuleTypeProperties extends ClassWithInterfaceProperties
{

    public ModuleTypeProperties()
    {
        this("MyModuleTypeProperties");
    }

    public ModuleTypeProperties(String fqClassName)
    {
        super(fqClassName);
    }
}
