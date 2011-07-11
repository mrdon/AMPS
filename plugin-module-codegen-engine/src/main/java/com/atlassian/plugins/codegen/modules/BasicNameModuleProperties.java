package com.atlassian.plugins.codegen.modules;

import com.atlassian.plugins.codegen.util.ClassnameUtil;
import org.apache.commons.lang.StringUtils;

import java.util.Properties;

/**
 * @since version
 */
public class BasicNameModuleProperties extends Properties implements PluginModuleProperties {

    private boolean includeExamples;
    private Properties i18nProperties;

    public BasicNameModuleProperties() {
        this("MyPluginModule");
    }

    public BasicNameModuleProperties(Properties properties) {
        this("MyPluginModule", properties);
    }

    public BasicNameModuleProperties(String moduleName, Properties properties) {
        super(properties);
        setModuleNameAndKey(moduleName);
        includeExamples = false;
        i18nProperties = new Properties();
    }

    public BasicNameModuleProperties(String moduleName) {
        super();
        setModuleNameAndKey(moduleName);
        includeExamples = false;
        i18nProperties = new Properties();
    }

    public void setModuleNameAndKey(String moduleName) {
        if (StringUtils.isNotBlank(moduleName)) {
            setProperty(MODULE_NAME, moduleName);
            setModuleKey(ClassnameUtil.camelCaseOrSpaceToDashed(moduleName).toLowerCase());
        }
    }

    private void setModuleName(String name) {
        setProperty(MODULE_NAME, name);
    }

    private void setModuleKey(String name) {
        setProperty(MODULE_KEY, name);
    }

    @Override
    public void setDescription(String desc) {
        setProperty(DESCRIPTION, desc);
    }

    @Override
    public void setDescriptionKey(String key) {
        setProperty(DESCRIPTION_KEY, key);
    }

    @Override
    public void setNameKey(String key) {
        setProperty(NAME_KEY, key);
    }

    @Override
    public void setIncludeExamples(boolean includeExamples) {
        this.includeExamples = includeExamples;
    }

    @Override
    public boolean includeExamples() {
        return includeExamples;
    }

    @Override
    public void addI18nProperty(String name, String value) {
        i18nProperties.setProperty(name, value);
    }

    @Override
    public Properties getI18nProperties() {
        return i18nProperties;
    }

}