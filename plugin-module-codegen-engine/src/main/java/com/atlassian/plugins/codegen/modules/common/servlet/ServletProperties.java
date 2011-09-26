package com.atlassian.plugins.codegen.modules.common.servlet;

import java.util.HashMap;
import java.util.Map;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;

/**
 * @since 3.5
 */
public class ServletProperties extends BasicClassModuleProperties
{
    public static final String URL_PATTERN = "URL_PATTERN";
    public static final String INIT_PARAMS = "INIT_PARAMS";

    public ServletProperties()
    {
        this("MyServlet");
    }

    public ServletProperties(String fqClassName)
    {
        super(fqClassName);
        put(INIT_PARAMS, new HashMap<String, String>());

        //sane defaults
        setUrlPattern("/" + getProperty(CLASSNAME).toLowerCase());
    }

    public void setUrlPattern(String pattern)
    {
        setProperty(URL_PATTERN, pattern);
    }

    public void setInitParams(Map<String, String> params)
    {
        put(INIT_PARAMS, params);
    }

    @SuppressWarnings(value = "unchecked")
    public void addInitParam(String name, String value)
    {
        Map<String, String> params = (Map<String, String>) get(INIT_PARAMS);
        if (params == null)
        {
            params = new HashMap<String, String>();
            setInitParams(params);
        }

        params.put(name, value);
    }
}
