package com.atlassian.plugins.codegen.modules.jira;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;
import com.atlassian.plugins.codegen.modules.common.Resource;

import java.util.*;

/**
 * @since version
 */
public class UserFormatProperties extends BasicClassModuleProperties {

    public static final String TYPE_NAME = "TYPE_NAME";
    public static final String TYPE_KEY = "TYPE_KEY";
    public static final String RESOURCES = "RESOURCES";

    public UserFormatProperties() {
        this("MyUserFormat");
    }

    public UserFormatProperties(String fqClassName) {
        super(fqClassName);
        setResources(new ArrayList<Resource>());
    }

    public void setTypeKey(String key) {
        setProperty(TYPE_KEY,key);
    }

    public String getTypeKey() {
        return getProperty(TYPE_KEY);
    }

    public void setTypeName(String name) {
        setProperty(TYPE_NAME,name);
    }

    public String getTypeName() {
        return getProperty(TYPE_NAME);
    }

    public void setResources(List<Resource> resources) {
        put(RESOURCES,resources);
    }

    public List<Resource> getResources() {
        return (List<Resource>)get(RESOURCES);
    }

    public void addResource(Resource resource) {
        List<Resource> resources = getResources();
        if(null == resources) {
            resources = new ArrayList<Resource>();
            setResources(resources);
        }

        resources.add(resource);
    }

    @Override
    public Properties getI18nProperties() {
        Properties props = super.getI18nProperties();

        if(containsKey(TYPE_KEY) && containsKey(TYPE_NAME)) {
            props.setProperty(getProperty(TYPE_KEY),getProperty(TYPE_NAME));
        }

        return props;
    }
}
