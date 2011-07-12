package com.atlassian.plugins.codegen.modules;

import com.atlassian.plugins.codegen.util.ClassnameUtil;
import org.apache.commons.lang.StringUtils;

import java.util.Properties;

/**
 * Author: jdoklovic
 */
public class BasicClassModuleProperties extends Properties implements PluginModuleProperties {

    private boolean includeExamples;
    private Properties i18nProperties;

    public BasicClassModuleProperties() {
        this("MyPluginModule");
    }

    public BasicClassModuleProperties(String fqClassName) {
        super();
        i18nProperties = new Properties();

        setFullyQualifiedClassname(fqClassName);

        String classname = getProperty(CLASSNAME);
        setModuleName(ClassnameUtil.camelCaseToSpaced(classname));
        setModuleKey(ClassnameUtil.camelCaseToDashed(classname).toLowerCase());
        setDescription("The " + getProperty(MODULE_NAME) + " Plugin");
        setNameI18nKey(getProperty(MODULE_KEY) + ".name");
        setDescriptionI18nKey(getProperty(MODULE_KEY) + ".description");

        addI18nProperty(getProperty(DESCRIPTION_I18N_KEY), getProperty(DESCRIPTION));
        addI18nProperty(getProperty(NAME_I18N_KEY), getProperty(MODULE_NAME));

        includeExamples = false;
    }

    public void setFullyQualifiedClassname(String fqName) {
        String classname;
        if (StringUtils.isNotBlank(fqName)) {
            if (fqName.lastIndexOf(".") > 0) {
                classname = StringUtils.substringAfterLast(fqName, ".");
                String packageName = StringUtils.substringBeforeLast(fqName, ".");
                setProperty(CLASSNAME, classname);
                if(StringUtils.isBlank(getProperty(FQ_CLASS_UNDER_TEST))) {
                    setClassUnderTest(fqName);
                }

                setProperty(PACKAGE, packageName);
            } else {
                classname = fqName;
                setProperty(CLASSNAME, classname);
                if(StringUtils.isBlank(getProperty(FQ_CLASS_UNDER_TEST))) {
                    setClassUnderTest(fqName);
                }
                setProperty(PACKAGE, "");
            }

            setProperty(FQ_CLASSNAME, fqName);
        }

    }

    public void setClassUnderTest(String fqName) {
        String classname;
        if (StringUtils.isNotBlank(fqName)) {
            if (fqName.lastIndexOf(".") > 0) {
                classname = StringUtils.substringAfterLast(fqName, ".");
                String packageName = StringUtils.substringBeforeLast(fqName, ".");
                setProperty(CLASS_UNDER_TEST, classname);
                setProperty(PACKAGE_UNDER_TEST, packageName);
            } else {
                classname = fqName;
                setProperty(CLASS_UNDER_TEST, classname);
                setProperty(PACKAGE_UNDER_TEST, "");
            }

            setProperty(FQ_CLASS_UNDER_TEST, fqName);
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
