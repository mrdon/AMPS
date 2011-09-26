package com.atlassian.plugins.codegen.modules.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 3.5
 */
public class Condition implements Conditional
{

    private final String fqClassName;
    private boolean invert;

    private final Map<String, String> params;

    public Condition(String fqClassName)
    {
        this.fqClassName = fqClassName;
        this.params = new HashMap<String, String>();
    }

    public String getFullyQualifiedClassName()
    {
        return fqClassName;
    }

    public Map<String, String> getParams()
    {
        return params;
    }

    public void addParam(String name, String value)
    {
        params.put(name, value);
    }

    public void setParams(Map<String, String> newParams)
    {
        params.putAll(newParams);
    }

    public boolean isParamsEmpty()
    {
        return params.isEmpty();
    }

    public boolean isInvert()
    {
        return invert;
    }

    public void setInvert(boolean invert)
    {
        this.invert = invert;
    }
}
