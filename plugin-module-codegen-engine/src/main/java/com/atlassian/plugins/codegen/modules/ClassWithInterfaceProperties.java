package com.atlassian.plugins.codegen.modules;

import org.apache.commons.lang.StringUtils;

/**
 * @since 3.5
 */
public class ClassWithInterfaceProperties extends BasicClassModuleProperties  {
    public static final String INTERFACE_CLASS = "INTERFACE_CLASS";
    public static final String FQ_INTERFACE = "FQ_INTERFACE";
    public static final String INTERFACE_PACKAGE = "INTERFACE_PACKAGE";

    public ClassWithInterfaceProperties() {
        this("MyClass");
    }

    public ClassWithInterfaceProperties(String fqClassName) {
        super(fqClassName);
    }

    public void setFullyQualifiedInterface(String fqName) {
        String classname;
        if (StringUtils.isNotBlank(fqName)) {

            if (fqName.lastIndexOf(".") > 0) {
                classname = StringUtils.substringAfterLast(fqName, ".");
                String packageName = StringUtils.substringBeforeLast(fqName, ".");
                setProperty(INTERFACE_CLASS, classname);
                setProperty(INTERFACE_PACKAGE, packageName);
            } else {
                classname = fqName;
                setProperty(INTERFACE_CLASS, classname);
                setProperty(INTERFACE_PACKAGE, "");
            }

            setProperty(FQ_INTERFACE, fqName);
        }
    }

    public String getFullyQualifiedInterface() {
        return getProperty(FQ_INTERFACE);
    }

    public String getInterfaceClass() {
        return getProperty(INTERFACE_CLASS);
    }

    public String getInterfacePackage() {
        return getProperty(INTERFACE_PACKAGE);
    }
}
