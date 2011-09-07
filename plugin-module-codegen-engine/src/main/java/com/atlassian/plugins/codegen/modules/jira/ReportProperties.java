package com.atlassian.plugins.codegen.modules.jira;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;
import com.atlassian.plugins.codegen.modules.common.Label;
import com.atlassian.plugins.codegen.modules.common.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @since 3.5
 */
public class ReportProperties extends BasicClassModuleProperties {
    public static final String LABEL = "LABEL";
    public static final String RESOURCES = "RESOURCES";

    public ReportProperties() {
        this("MyReport");
    }

    public ReportProperties(String fqClassName) {
        super(fqClassName);
        setResources(new ArrayList<Resource>());
    }

    public void setLabel(Label label) {
        put(LABEL,label);
        addI18nProperty(label.getKey(),label.getValue());
    }

    public Label getLabel() {
        Label label = null;
        if(keySet().contains(LABEL)) {
            label = (Label)get(LABEL);
        }
        return label;
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
}
