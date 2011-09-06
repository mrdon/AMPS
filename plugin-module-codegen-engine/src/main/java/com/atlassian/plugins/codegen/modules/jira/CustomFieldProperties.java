package com.atlassian.plugins.codegen.modules.jira;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;
import com.atlassian.plugins.codegen.modules.common.Resource;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @since 3.5
 */
public class CustomFieldProperties extends BasicClassModuleProperties {

    public static final String RESOURCES = "RESOURCES";
    public static final String CLASS_TO_EXTEND = "CLASS_TO_EXTEND";
    public static final String PACKAGE_TO_EXTEND = "PACKAGE_TO_EXTEND";
    public static final String FQ_CLASS_TO_EXTEND = "FQ_CLASS_TO_EXTEND";


    public CustomFieldProperties() {
        this("MyCustomField");
    }

    public CustomFieldProperties(String fqClassName) {
        super(fqClassName);
        setResources(new ArrayList<Resource>());
        setFullyQualifiedClassToExtend("com.atlassian.jira.issue.customfields.impl.TextCFType");
    }

    public void setResources(List<Resource> resources) {
        put(RESOURCES,resources);
    }

    public List<Resource> getResources() {
        return (List<Resource>)get(RESOURCES);
    }

    public void addResource(Resource resource) {
        List<Resource> resources = getResources();
        if(null == resources) {
            resources = new ArrayList<Resource>();
            setResources(resources);
        }

        resources.add(resource);
    }

    public void setFullyQualifiedClassToExtend(String fqName) {
        String classname;
        if (StringUtils.isNotBlank(fqName)) {
            if (fqName.lastIndexOf(".") > 0) {
                classname = StringUtils.substringAfterLast(fqName, ".");
                String packageName = StringUtils.substringBeforeLast(fqName, ".");
                setProperty(CLASS_TO_EXTEND, classname);
                setProperty(PACKAGE_TO_EXTEND, packageName);
            } else {
                classname = fqName;
                setProperty(CLASS_TO_EXTEND, classname);
                setProperty(PACKAGE_TO_EXTEND, "");
            }

            setProperty(FQ_CLASS_TO_EXTEND, fqName);
        }

    }

    public String getFullyQualifiedClassToExtend() {
        return getProperty(FQ_CLASS_TO_EXTEND);
    }

}
