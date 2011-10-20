package com.atlassian.plugins.codegen.modules.jira;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;

/**
 * @since 3.6
 */
public class JqlFunctionProperties extends BasicClassModuleProperties
{

    public JqlFunctionProperties()
    {
        this("MyJqlFunction");
    }

    public JqlFunctionProperties(String fqClassName)
    {
        super(fqClassName);
    }
}
