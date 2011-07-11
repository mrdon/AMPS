package com.atlassian.plugins.codegen.modules.common.servlet;


import com.atlassian.plugins.codegen.modules.BasicNameModuleProperties;

/**
 * @since version
 */
public class ServletContextParameterProperties extends BasicNameModuleProperties {
    private static final String KEY_PREFIX = "jira.servlet.parameter.";
    public static final String PARAM_NAME = "PARAM_NAME";
    public static final String PARAM_VALUE = "PARAM_VALUE";

    public ServletContextParameterProperties() {
        this("MyServletContextParameter");
    }

    public ServletContextParameterProperties(String fqClassName) {
        super(fqClassName);

        //sane defaults
        setDescription("The " + getProperty(MODULE_NAME) + " ServletContextParameter");
        setDescriptionKey(KEY_PREFIX + getProperty(MODULE_KEY) + ".description");
        addI18nProperty(getProperty(DESCRIPTION_KEY), getProperty(DESCRIPTION));

        setNameKey(KEY_PREFIX + getProperty(MODULE_KEY) + ".name");
        addI18nProperty(getProperty(NAME_KEY), getProperty(MODULE_NAME));
    }

    public void setParamName(String name) {
        setProperty(PARAM_NAME,name);
    }

    public void setParamValue(String value) {
        setProperty(PARAM_VALUE,value);
    }
}
