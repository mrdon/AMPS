package com.atlassian.plugins.codegen.modules.jira;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @since version
 */
public class WorkflowElementProperties extends BasicClassModuleProperties {
    public static final String FQ_FACTORY_NAME = "FQ_FACTORY_NAME";
    public static final String FACTORY_NAME = "FACTORY_NAME";

    public WorkflowElementProperties() {
        this("MyWorkflowElement");
    }

    public WorkflowElementProperties(String fqClassName) {
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

    public String getFullyQualifiedFactoryName() {
        return getProperty(FQ_FACTORY_NAME);
    }

    public String getFactoryName() {
        return getProperty(FACTORY_NAME);
    }
}
