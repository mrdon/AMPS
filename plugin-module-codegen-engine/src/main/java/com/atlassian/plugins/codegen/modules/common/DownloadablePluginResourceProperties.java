package com.atlassian.plugins.codegen.modules.common;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @since version
 */
public class DownloadablePluginResourceProperties extends Properties implements PluginModuleProperties {
    private boolean includeExamples;

    public static final String RESOURCE = "RESOURCE";

    public DownloadablePluginResourceProperties(Resource resource) {
        super();
        includeExamples = false;
        put(RESOURCE,resource);
    }

    public Resource getResource() {
        return (Resource)get(RESOURCE);
    }

    @Override
    public void setProductId(String id) {
        setProperty(PRODUCT_ID,id);
    }

    @Override
    public String getProductId() {
        return getProperty(PRODUCT_ID);
    }
    
    @Override
    public void setDescription(String desc) {}

    @Override
    public void setDescriptionI18nKey(String key) {}

    @Override
    public void setNameI18nKey(String key) {}

    @Override
    public void setIncludeExamples(boolean includeExamples) {
        this.includeExamples = includeExamples;
    }

    @Override
    public boolean includeExamples() {
        return includeExamples;
    }

    @Override
    public void addI18nProperty(String name, String value) {}

    @Override
    public Properties getI18nProperties() {
        return new Properties();
    }

    @Override
    public void setModuleName(String name) {}

    @Override
    public void setModuleKey(String name) {}
}
