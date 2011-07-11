package com.atlassian.plugins.codegen.modules.common.servlet;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;

/**
 * @since version
 */
public class ServletContextListenerProperties extends BasicClassModuleProperties {
    private static final String KEY_PREFIX = "jira.servlet.listener.";

    public ServletContextListenerProperties() {
        this("MyServletContextListener");
    }

    public ServletContextListenerProperties(String fqClassName) {
        super(fqClassName);

        //sane defaults
        setDescription("The " + getProperty(MODULE_NAME) + " ServletContextListener");
        setDescriptionKey(KEY_PREFIX + getProperty(MODULE_KEY) + ".description");
        addI18nProperty(getProperty(DESCRIPTION_KEY), getProperty(DESCRIPTION));

        setNameKey(KEY_PREFIX + getProperty(MODULE_KEY) + ".name");
        addI18nProperty(getProperty(NAME_KEY), getProperty(MODULE_NAME));
    }
}
