package com.atlassian.plugins.codegen.modules.jira;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;
import com.atlassian.plugins.codegen.modules.common.Resource;

/**
 * @since 3.6
 */
public class SearchRequestViewProperties extends BasicClassModuleProperties
{

    public static final String FILE_EXTENSION = "FILE_EXTENSION";
    public static final String CONTENT_TYPE = "CONTENT_TYPE";
    public static final String RESOURCES = "RESOURCES";
    public static final String ORDER = "ORDER";

    public SearchRequestViewProperties()
    {
        this("MySearchRequestView");
    }

    public SearchRequestViewProperties(String fqClassName)
    {
        super(fqClassName);
        setResources(new ArrayList<Resource>());
        setOrder(10);
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

    public void setContentType(String type)
    {
        setProperty(CONTENT_TYPE, type);
    }

    public String getContentType()
    {
        return getProperty(CONTENT_TYPE);
    }

    public void setFileExtension(String ext)
    {
        setProperty(FILE_EXTENSION, ext);
    }

    public String getFileExtension()
    {
        return getProperty(FILE_EXTENSION);
    }

    public void setOrder(int order)
    {
        setProperty(ORDER, Integer.toString(order));
    }

    public String getOrder()
    {
        return getProperty(ORDER);
    }

    public int getOrderAsInt()
    {
        return Integer.parseInt(getProperty(ORDER));
    }
}
