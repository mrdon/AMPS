package com.atlassian.plugins.codegen.modules.common.servlet;


import com.atlassian.plugins.codegen.modules.BasicNameModuleProperties;

/**
 * @since version
 */
public class ServletContextParameterProperties extends BasicNameModuleProperties {
    public static final String PARAM_NAME = "PARAM_NAME";
    public static final String PARAM_VALUE = "PARAM_VALUE";

    public ServletContextParameterProperties() {
        this("MyServletContextParameter");
    }

    public ServletContextParameterProperties(String fqClassName) {
        super(fqClassName);
    }

    public void setParamName(String name) {
        setProperty(PARAM_NAME,name);
    }

    public void setParamValue(String value) {
        setProperty(PARAM_VALUE,value);
    }
}
