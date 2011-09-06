package com.atlassian.plugins.codegen.modules;

import com.atlassian.plugins.codegen.util.ClassnameUtil;
import org.apache.commons.lang.StringUtils;

import java.util.Properties;

/**
 * @since 3.5
 */
public abstract class AbstractNameBasedModuleProperties extends AbstractPluginModuleProperties implements NameBasedModuleProperties {

    protected AbstractNameBasedModuleProperties() {
        super();
    }

    public void setModuleNameAndKey(String moduleName) {
        if (StringUtils.isNotBlank(moduleName)) {
            setModuleName(moduleName);
            setModuleKey(ClassnameUtil.camelCaseOrSpaceToDashed(moduleName).toLowerCase());
            setDescription("The " + getProperty(MODULE_NAME) + " Plugin");
            setNameI18nKey(getProperty(MODULE_KEY) + ".name");
            setDescriptionI18nKey(getProperty(MODULE_KEY) + ".description");
        }
    }

    @Override
    public void setModuleName(String name) {
        setProperty(MODULE_NAME, name);
    }

    @Override
    public String getModuleName() {
        return getProperty(MODULE_NAME);
    }

    @Override
    public void setModuleKey(String name) {
        setProperty(MODULE_KEY, name);
    }

    @Override
    public String getModuleKey() {
        return getProperty(MODULE_KEY);
    }

    @Override
    public void setDescription(String desc) {
        setProperty(DESCRIPTION, desc);
    }

    @Override
    public String getDescription() {
        return getProperty(DESCRIPTION);
    }

    @Override
    public void setDescriptionI18nKey(String key) {
        setProperty(DESCRIPTION_I18N_KEY, key);
    }

    @Override
    public String getDescriptionI18nKey() {
        return getProperty(DESCRIPTION_I18N_KEY);
    }

    @Override
    public void setNameI18nKey(String key) {
        setProperty(NAME_I18N_KEY, key);
    }

    @Override
    public String getNameI18nKey() {
        return getProperty(NAME_I18N_KEY);
    }

    @Override
    public Properties getI18nProperties() {
        Properties props = super.getI18nProperties();
        props.setProperty(getProperty(NAME_I18N_KEY),getProperty(MODULE_NAME));
        props.setProperty(getProperty(DESCRIPTION_I18N_KEY),getProperty(DESCRIPTION));

        return props;
    }
}
