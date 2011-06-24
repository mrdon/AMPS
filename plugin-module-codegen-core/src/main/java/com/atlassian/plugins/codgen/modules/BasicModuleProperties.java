package com.atlassian.plugins.codgen.modules;

import com.atlassian.plugins.codgen.util.ClassnameUtil;
import org.apache.commons.lang.StringUtils;

import java.util.Properties;

/**
 * Author: jdoklovic
 */
public class BasicModuleProperties extends Properties {
    public static final String FQ_CLASSNAME = "FQ_CLASSNAME";
    public static final String CLASSNAME = "CLASSNAME";
    public static final String PACKAGE = "PACKAGE";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String DESCRIPTION_KEY = "DESCRIPTION_KEY";
    public static final String MODULE_NAME = "MODULE_NAME";
    public static final String MODULE_KEY = "MODULE_KEY";
    public static final String NAME_KEY = "NAME_KEY";

    private boolean includeExamples;
    private Properties i18nProperties;

    public BasicModuleProperties() {
        this("MyPluginModule");
    }

    public BasicModuleProperties(String fqClassName) {
        super();
        setFullyQualifiedClassname(fqClassName);
        includeExamples = false;
        i18nProperties = new Properties();
    }

    public void setFullyQualifiedClassname(String fqName) {
        String classname;
        if (StringUtils.isNotBlank(fqName)) {
            if (fqName.lastIndexOf(".") > 0) {
                classname = StringUtils.substringAfterLast(fqName, ".");
                String packageName = StringUtils.substringBeforeLast(fqName, ".");
                setProperty(CLASSNAME, classname);
                setProperty(PACKAGE, packageName);
            } else {
                classname = fqName;
                setProperty(CLASSNAME, classname);
                setProperty(PACKAGE, "");
            }

            setProperty(FQ_CLASSNAME, fqName);
            setModuleName(ClassnameUtil.camelCaseToSpaced(classname).toLowerCase());
            setModuleKey(ClassnameUtil.camelCaseToDashed(classname).toLowerCase());
        }

    }

    private void setModuleName(String name) {
        setProperty(MODULE_NAME, name);
    }

    private void setModuleKey(String name) {
        setProperty(MODULE_KEY, name);
    }

    public void setDescription(String desc) {
        setProperty(DESCRIPTION, desc);
    }

    public void setDescriptionKey(String key) {
        setProperty(DESCRIPTION_KEY, key);
    }

    public void setNameKey(String key) {
        setProperty(NAME_KEY, key);
    }

    public void setIncludeExamples(boolean includeExamples) {
        this.includeExamples = includeExamples;
    }

    public boolean includeExamples() {
        return includeExamples;
    }

    public void addI18nProperty(String name, String value) {
        i18nProperties.setProperty(name, value);
    }

    public Properties getI18nProperties() {
        return i18nProperties;
    }

}
