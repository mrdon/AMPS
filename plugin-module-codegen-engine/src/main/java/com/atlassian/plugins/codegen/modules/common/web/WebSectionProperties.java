package com.atlassian.plugins.codegen.modules.common.web;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;
import com.atlassian.plugins.codegen.modules.common.Label;
import com.atlassian.plugins.codegen.modules.common.Tooltip;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 3.5
 */
public class WebSectionProperties extends AbstractWebFragmentProperties {

    public static final String LOCATION = "LOCATION";
    public static final String LABEL = "LABEL";
    public static final String PARAMS = "PARAMS";
    public static final String TOOLTIP = "TOOLTIP";

    public WebSectionProperties() {
        this("My Web Section");
    }

    public WebSectionProperties(String moduleName) {
        super(moduleName);
        setParams(new HashMap<String, String>());
    }

    public WebSectionProperties(String moduleName, String location) {
        this(moduleName);
        setLocation(location);
    }

    public void setLocation(String location) {
        setProperty(LOCATION,location);
    }

    public String getLocation() {
        return getProperty(LOCATION);
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

    public void setTooltip(Tooltip tooltip) {
        put(TOOLTIP,tooltip);
        addI18nProperty(tooltip.getKey(),tooltip.getValue());
    }

    public Tooltip getTooltip() {
        Tooltip tooltip = null;
        if(keySet().contains(TOOLTIP)) {
            tooltip = (Tooltip)get(TOOLTIP);
        }
        return tooltip;
    }

    public void setParams(Map<String,String> params) {
        put(PARAMS,params);
    }

    public void addParam(String name, String value) {
        ((Map<String,String>)get(PARAMS)).put(name,value);
    }
}
