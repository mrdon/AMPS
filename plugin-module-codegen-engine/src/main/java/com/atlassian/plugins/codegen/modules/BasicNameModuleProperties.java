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

    public BasicNameModuleProperties(String moduleName) {
        super();
        i18nProperties = new Properties();
        setModuleNameAndKey(moduleName);
        includeExamples = false;

    }

    public void setModuleNameAndKey(String moduleName) {
        if (StringUtils.isNotBlank(moduleName)) {
            setModuleName(moduleName);
            setModuleKey(ClassnameUtil.camelCaseOrSpaceToDashed(moduleName).toLowerCase());
            setDescription("The " + getProperty(MODULE_NAME) + " Plugin");
            setNameI18nKey(getProperty(MODULE_KEY) + ".name");
            setDescriptionI18nKey(getProperty(MODULE_KEY) + ".description");

            addI18nProperty(getProperty(DESCRIPTION_I18N_KEY), getProperty(DESCRIPTION));
            addI18nProperty(getProperty(NAME_I18N_KEY), getProperty(MODULE_NAME));
        }
    }

    @Override
    public void setModuleName(String name) {
        setProperty(MODULE_NAME, name);
    }

    @Override
    public void setModuleKey(String name) {
        setProperty(MODULE_KEY, name);
    }

    @Override
    public void setDescription(String desc) {
        setProperty(DESCRIPTION, desc);
    }

    @Override
    public void setDescriptionI18nKey(String key) {
        setProperty(DESCRIPTION_I18N_KEY, key);
    }

    @Override
    public void setNameI18nKey(String key) {
        setProperty(NAME_I18N_KEY, key);
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