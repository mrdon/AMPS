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
    String DESCRIPTION = "DESCRIPTION";
    String DESCRIPTION_KEY = "DESCRIPTION_KEY";
    String MODULE_NAME = "MODULE_NAME";
    String MODULE_KEY = "MODULE_KEY";
    String NAME_KEY = "NAME_KEY";

    Enumeration<?> propertyNames();

    Object setProperty(java.lang.String s, java.lang.String s1);

    String getProperty(java.lang.String s);

    void setFullyQualifiedClassname(String fqName);

    void setDescription(String desc);

    void setDescriptionKey(String key);

    void setNameKey(String key);

    void setIncludeExamples(boolean includeExamples);

    boolean includeExamples();

    void addI18nProperty(String name, String value);

    Properties getI18nProperties();
}
