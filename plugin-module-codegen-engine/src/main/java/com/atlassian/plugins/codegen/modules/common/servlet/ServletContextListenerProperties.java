package com.atlassian.plugins.codegen.modules.common.servlet;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;

/**
 * @since 3.6
 */
public class ServletContextListenerProperties extends BasicClassModuleProperties
{

    public ServletContextListenerProperties()
    {
        this("MyServletContextListener");
    }

    public ServletContextListenerProperties(String fqClassName)
    {
        super(fqClassName);
    }
}
