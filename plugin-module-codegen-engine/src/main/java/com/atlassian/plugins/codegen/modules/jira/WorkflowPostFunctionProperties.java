package com.atlassian.plugins.codegen.modules.jira;

import com.atlassian.plugins.codegen.modules.BasicModuleProperties;
import org.apache.commons.lang.StringUtils;

/**
 * Author: jdoklovic
 */
public class WorkflowPostFunctionProperties extends BasicModuleProperties {

    public static final String FQ_FACTORY_NAME = "FQ_FACTORY_NAME";
    public static final String FACTORY_NAME = "FACTORY_NAME";
    public static final String ORDERABLE = "ORDERABLE";
    public static final String UNIQUE = "UNIQUE";
    public static final String DELETABLE = "DELETABLE";

    private WorkflowPostFunctionProperties(String fqClassName) {
        super(fqClassName);
    }

    @Override
    public void setFullyQualifiedClassname(String fqName) {
        super.setFullyQualifiedClassname(fqName);
        setFullyQualifiedFactoryName(fqName + "Factory");
    }

    public void setFullyQualifiedFactoryName(String fqName) {
        setProperty(FQ_FACTORY_NAME, fqName);
        String classname;

        if (fqName.lastIndexOf(".") > 0) {
            classname = StringUtils.substringAfterLast(fqName, ".");
        } else {
            classname = fqName;
        }

        setProperty(FACTORY_NAME, classname);
    }

    public void setOrderable(boolean orderable) {
        setProperty(ORDERABLE, Boolean.toString(orderable));
    }

    public void setUnique(boolean unique) {
        setProperty(UNIQUE, Boolean.toString(unique));
    }

    public void setDeletable(boolean deletable) {
        setProperty(DELETABLE, Boolean.toString(deletable));
    }
}
