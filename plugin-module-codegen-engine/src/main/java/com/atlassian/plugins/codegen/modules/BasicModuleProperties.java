package com.atlassian.plugins.codegen.modules;

import com.atlassian.plugins.codegen.util.ClassnameUtil;
import org.apache.commons.lang.StringUtils;

import java.util.Properties;

/**
 * Author: jdoklovic
 */
public class BasicModuleProperties extends Properties implements PluginModuleProperties {

    private boolean includeExamples;
    private Properties i18nProperties;

    public BasicModuleProperties() {
        this("MyPluginModule");
    }

    public BasicModuleProperties(Properties properties) {
        this("MyPluginModule", properties);
    }

    public BasicModuleProperties(String fqClassName, Properties properties) {
        super(properties);
        setFullyQualifiedClassname(fqClassName);
        includeExamples = false;
        i18nProperties = new Properties();
    }

    public BasicModuleProperties(String fqClassName) {
        super();
        setFullyQualifiedClassname(fqClassName);
        includeExamples = false;
        i18nProperties = new Properties();
    }

    @Override
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
