package com.atlassian.plugins.codegen.modules;

import java.util.Properties;

/**
 * @since 3.5
 */
public interface NameBasedModuleProperties extends PluginModuleProperties {
    String DESCRIPTION = "DESCRIPTION";
    String DESCRIPTION_I18N_KEY = "DESCRIPTION_I18N_KEY";
    String MODULE_NAME = "MODULE_NAME";
    String MODULE_KEY = "MODULE_KEY";
    String NAME_I18N_KEY = "NAME_I18N_KEY";

    void setDescription(String desc);

    String getDescription();

    void setDescriptionI18nKey(String key);

    String getDescriptionI18nKey();

    void setNameI18nKey(String key);

    String getNameI18nKey();

    void setModuleName(String name);

    String getModuleName();

    void setModuleKey(String name);

    String getModuleKey();
}
