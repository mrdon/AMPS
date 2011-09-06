package com.atlassian.plugins.codegen.modules.jira;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;
import org.apache.commons.lang.StringUtils;

/**
 *
 */
public class WorkflowPostFunctionProperties extends WorkflowElementProperties {

    public static final String ORDERABLE = "ORDERABLE";
    public static final String UNIQUE = "UNIQUE";
    public static final String DELETABLE = "DELETABLE";
    public static final String ADDABLE = "ADDABLE";

    public WorkflowPostFunctionProperties(String fqClassName) {
        super(fqClassName);
    }

    public void setOrderable(boolean orderable) {
        setProperty(ORDERABLE, Boolean.toString(orderable));
    }

    public String getOrderable() {
        return getProperty(ORDERABLE);
    }

    public void setUnique(boolean unique) {
        setProperty(UNIQUE, Boolean.toString(unique));
    }

    public String getUnique() {
        return getProperty(UNIQUE);
    }

    public void setDeletable(boolean deletable) {
        setProperty(DELETABLE, Boolean.toString(deletable));
    }

    public String getDeletable() {
        return getProperty(DELETABLE);
    }

    public void setAddable(String addable) {
        setProperty(ADDABLE, addable);
    }

    public String getAddable() {
        return getProperty(ADDABLE);
    }
}
