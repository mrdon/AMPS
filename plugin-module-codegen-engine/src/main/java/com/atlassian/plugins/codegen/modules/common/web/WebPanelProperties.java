package com.atlassian.plugins.codegen.modules.common.web;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @since version
 */
public class WebPanelProperties extends AbstractWebFragmentProperties {

    public static final String LOCATION = "LOCATION";

    public WebPanelProperties() {
        this("My Web Panel");
    }

    public WebPanelProperties(String moduleName) {
        super(moduleName);
    }

    public WebPanelProperties(String moduleName, String location) {
        this(moduleName);
        setLocation(location);
    }

    public void setLocation(String location) {
        setProperty(LOCATION,location);
    }

    public String getLocation() {
        return getProperty(LOCATION);
    }
}
