package com.atlassian.plugins.codegen.modules;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

/**
 * @since version
 */
public interface PluginModuleProperties extends Map<Object,Object> {
    String FQ_CLASSNAME = "FQ_CLASSNAME";
    String CLASSNAME = "CLASSNAME";
    String PACKAGE = "PACKAGE";
    String CLASS_UNDER_TEST = "CLASS_UNDER_TEST";
    String PACKAGE_UNDER_TEST = "PACKAGE_UNDER_TEST";
    String FQ_CLASS_UNDER_TEST = "FQ_CLASS_UNDER_TEST";
    String DESCRIPTION = "DESCRIPTION";
    String DESCRIPTION_I18N_KEY = "DESCRIPTION_I18N_KEY";
    String MODULE_NAME = "MODULE_NAME";
    String MODULE_KEY = "MODULE_KEY";
    String NAME_I18N_KEY = "NAME_I18N_KEY";
    String PRODUCT_ID = "PRODUCT_ID";

    Enumeration<?> propertyNames();

    Object setProperty(java.lang.String s, java.lang.String s1);

    String getProperty(java.lang.String s);

    void setDescription(String desc);

    void setDescriptionI18nKey(String key);

    void setNameI18nKey(String key);

    void setIncludeExamples(boolean includeExamples);

    boolean includeExamples();

    void addI18nProperty(String name, String value);

    Properties getI18nProperties();

    void setModuleName(String name);

    void setModuleKey(String name);

    void setProductId(String id);

    String getProductId();
}
