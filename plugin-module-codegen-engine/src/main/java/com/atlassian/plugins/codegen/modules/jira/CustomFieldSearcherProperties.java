package com.atlassian.plugins.codegen.modules.jira;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;
import com.atlassian.plugins.codegen.modules.common.Resource;

/**
 * @since 3.5
 */
public class CustomFieldSearcherProperties extends BasicClassModuleProperties
{

    public static final String VALID_CUSTOM_FIELD_PACKAGE = "VALID_CUSTOM_FIELD_PACKAGE";
    public static final String VALID_CUSTOM_FIELD_KEY = "VALID_CUSTOM_FIELD_KEY";
    public static final String RESOURCES = "RESOURCES";

    private boolean generateClass;

    public CustomFieldSearcherProperties()
    {
        this("MyCustomFieldSearcher");
    }

    public CustomFieldSearcherProperties(String fqClassName)
    {
        super(fqClassName);
        setResources(new ArrayList<Resource>());
        this.generateClass = false;
    }

    public void setResources(List<Resource> resources)
    {
        put(RESOURCES, resources);
    }

    public List<Resource> getResources()
    {
        return (List<Resource>) get(RESOURCES);
    }

    public void addResource(Resource resource)
    {
        List<Resource> resources = getResources();
        if (null == resources)
        {
            resources = new ArrayList<Resource>();
            setResources(resources);
        }

        resources.add(resource);
    }

    public boolean generateClass()
    {
        return generateClass;
    }

    public void setGenerateClass(boolean generateClass)
    {
        this.generateClass = generateClass;
    }

    public void setValidCustomFieldPackage(String packageName)
    {
        setProperty(VALID_CUSTOM_FIELD_PACKAGE, packageName);
    }

    public String getValidCustomFieldPackage()
    {
        return getProperty(VALID_CUSTOM_FIELD_PACKAGE);
    }

    public void setValidCustomFieldKey(String key)
    {
        setProperty(VALID_CUSTOM_FIELD_KEY, key);
    }

    public String getValidCustomFieldKey()
    {
        return getProperty(VALID_CUSTOM_FIELD_KEY);
    }
}
