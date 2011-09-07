package com.atlassian.plugins.codegen.modules;

import org.apache.commons.lang.StringUtils;

/**
 * @since 3.5
 */
public abstract class AbstractClassBasedModuleProperties extends AbstractNameBasedModuleProperties implements ClassBasedModuleProperties {

    protected AbstractClassBasedModuleProperties() {
        super();
    }

    public void setFullyQualifiedClassname(String fqName) {
        String classname;
        if (StringUtils.isNotBlank(fqName)) {
            if (fqName.lastIndexOf(".") > 0) {
                classname = StringUtils.substringAfterLast(fqName, ".");
                String packageName = StringUtils.substringBeforeLast(fqName, ".");
                setProperty(CLASSNAME, classname);
                setClassUnderTest(fqName);

                setProperty(PACKAGE, packageName);
            } else {
                classname = fqName;
                setProperty(CLASSNAME, classname);
                setClassUnderTest(fqName);
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
    public String getFullyQualifiedClassname() {
        return getProperty(FQ_CLASSNAME);
    }

    @Override
    public String getClassname() {
        return getProperty(CLASSNAME);
    }

    @Override
    public String getPackage() {
        return getProperty(PACKAGE);
    }

    @Override
    public String getClassUnderTest() {
        return getProperty(CLASS_UNDER_TEST);
    }

    @Override
    public String getFullyQualifiedClassUnderTest() {
        return getProperty(FQ_CLASS_UNDER_TEST);
    }

    @Override
    public String getPackageUnderTest() {
        return getProperty(PACKAGE_UNDER_TEST);
    }
}
