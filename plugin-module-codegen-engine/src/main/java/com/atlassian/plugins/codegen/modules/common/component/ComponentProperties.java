package com.atlassian.plugins.codegen.modules.common.component;

import java.util.HashMap;
import java.util.Map;

import com.atlassian.plugins.codegen.modules.ClassWithInterfaceProperties;

/**
 * @since 3.6
 */
public class ComponentProperties extends ClassWithInterfaceProperties
{

    public static final String ALIAS = "ALIAS";
    public static final String PUBLIC = "PUBLIC";
    public static final String SERVICE_PROPS = "SERVICE_PROPS";

    private boolean generateInterface;
    private boolean generateClass;

    public ComponentProperties()
    {
        this("MyComponentProperties");
    }

    public ComponentProperties(String fqClassName)
    {
        super(fqClassName);
        put(SERVICE_PROPS, new HashMap<String, String>());

        setAlias("");
        setPublic(false);
    }

    public void setAlias(String alias)
    {
        setProperty(ALIAS, alias);
    }

    public void setPublic(boolean isPublic)
    {
        setProperty(PUBLIC, Boolean.toString(isPublic));
    }

    public void setServiceProps(Map<String, String> params)
    {
        put(SERVICE_PROPS, params);
    }

    @SuppressWarnings(value = "unchecked")
    public void addServiceProp(String name, String value)
    {
        Map<String, String> params = (Map<String, String>) get(SERVICE_PROPS);
        if (params == null)
        {
            params = new HashMap<String, String>();
            setServiceProps(params);
        }

        params.put(name, value);
    }

    public boolean generateInterface()
    {
        return generateInterface;
    }

    public void setGenerateInterface(boolean generateInterface)
    {
        this.generateInterface = generateInterface;
    }

    public boolean generateClass()
    {
        return generateClass;
    }

    public void setGenerateClass(boolean generateClass)
    {
        this.generateClass = generateClass;
    }
}
