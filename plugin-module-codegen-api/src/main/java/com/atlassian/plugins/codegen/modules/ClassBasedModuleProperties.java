package com.atlassian.plugins.codegen.modules;

/**
 * @since 3.5
 */
public interface ClassBasedModuleProperties extends NameBasedModuleProperties
{
    String FQ_CLASSNAME = "FQ_CLASSNAME";
    String CLASSNAME = "CLASSNAME";
    String PACKAGE = "PACKAGE";
    String CLASS_UNDER_TEST = "CLASS_UNDER_TEST";
    String PACKAGE_UNDER_TEST = "PACKAGE_UNDER_TEST";
    String FQ_CLASS_UNDER_TEST = "FQ_CLASS_UNDER_TEST";

    String getFullyQualifiedClassname();

    String getClassname();

    String getPackage();

    String getClassUnderTest();

    String getFullyQualifiedClassUnderTest();

    String getPackageUnderTest();
}
