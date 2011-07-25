package com.atlassian.plugins.codegen.modules.common.web;

import com.atlassian.plugins.codegen.modules.BasicNameModuleProperties;
import com.atlassian.plugins.codegen.modules.common.Conditional;
import com.atlassian.plugins.codegen.modules.common.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * @since version
 */
public abstract class AbstractWebFragmentProperties extends AbstractConditionsProperties {

    public static final String CONTEXT_PROVIDER = "CONTEXT_PROVIDER";
    public static final String RESOURCES = "RESOURCES";
    public static final String WEIGHT = "WEIGHT";

    public AbstractWebFragmentProperties() {
        this("My Web Module");
    }

    public AbstractWebFragmentProperties(String moduleName) {
        super(moduleName);
        setResources(new ArrayList<Resource>());
        setWeight(1000);
    }

    public void setContextProvider(String fqClassname) {
        setProperty(CONTEXT_PROVIDER,fqClassname);
    }

    public void setResources(List<Resource> resources) {
        put(RESOURCES,resources);
    }

    public List<Resource> getResources() {
        return (List<Resource>)get(RESOURCES);
    }

    public void setWeight(int weight) {
        setProperty(WEIGHT, Integer.toString(weight));
    }

}
