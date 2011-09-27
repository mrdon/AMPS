package com.atlassian.plugins.codegen.modules.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 3.6
 */
public class Resource
{
    private String name;
    private String type;
    private String namePattern;
    private String location;
    private Map<String, String> params;
    private boolean batch;

    public Resource()
    {
        this.params = new HashMap<String, String>();
        this.batch = true;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getNamePattern()
    {
        return namePattern;
    }

    public void setNamePattern(String namePattern)
    {
        this.namePattern = namePattern;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public Map<String, String> getParams()
    {
        return params;
    }

    public void setParams(Map<String, String> params)
    {
        this.params = params;
    }

    public void addParam(String name, String value)
    {
        params.put(name, value);
    }

    public boolean isBatch()
    {
        return batch;
    }

    public void setBatch(boolean batch)
    {
        this.batch = batch;
    }
}
