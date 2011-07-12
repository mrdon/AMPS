package com.atlassian.plugins.codegen.modules.common.servlet;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;

/**
 * @since version
 */
public class ServletContextListenerProperties extends BasicClassModuleProperties {

    public ServletContextListenerProperties() {
        this("MyServletContextListener");
    }

    public ServletContextListenerProperties(String fqClassName) {
        super(fqClassName);
    }
}
