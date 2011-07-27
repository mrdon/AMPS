package com.atlassian.plugins.codegen.modules.common.component;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @since version
 */
public class ComponentImportProperties extends Properties implements PluginModuleProperties {
    public static final String FILTER = "FILTER";
    public static final String FQ_INTERFACE = "FQ_INTERFACE";

    public ComponentImportProperties() {
        this("MyComponentImportProperties");
    }

    public ComponentImportProperties(String fqClassName) {
        if(StringUtils.isNotBlank(fqClassName)) {
            setFullyQualifiedInterface(fqClassName);
            setModuleKey(StringUtils.uncapitalize(StringUtils.substringAfterLast(fqClassName,".")));
        }

        setFilter("");
    }

    public void setFilter(String filter) {
        setProperty(FILTER,filter);
    }

    public void setFullyQualifiedInterface(String fqName) {
        setProperty(FQ_INTERFACE,fqName);
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
    public void setDescription(String desc) {
    }

    @Override
    public void setDescriptionI18nKey(String key) {
    }

    @Override
    public void setNameI18nKey(String key) {
    }

    @Override
    public void setIncludeExamples(boolean includeExamples) {
    }

    @Override
    public boolean includeExamples() {
        return false;
    }

    @Override
    public void addI18nProperty(String name, String value) {
    }

    @Override
    public Properties getI18nProperties() {
        return new Properties();
    }

    @Override
    public void setModuleName(String name) {
    }

    @Override
    public void setModuleKey(String name) {
        setProperty(MODULE_KEY,name);
    }
}
