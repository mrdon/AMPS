package com.atlassian.plugins.codegen.modules.common.servlet;

import com.atlassian.plugins.codegen.modules.BasicModuleProperties;

import java.util.*;

/**
 * Author: jdoklovic
 */
public class ServletFilterProperties extends BasicModuleProperties {
    private static final String KEY_PREFIX = "jira.servlet.filter.";
    public static final String LOCATION = "LOCATION";
    public static final String WEIGHT = "WEIGHT";
    public static final String URL_PATTERN = "URL_PATTERN";
    public static final String DISPATCHERS = "DISPATCHERS";
    public static final String INIT_PARAMS = "INIT_PARAMS";

    public static final List<String> ALLOWED_LOCATIONS = initLocations();
    public static final List<String> ALLOWED_DISPATCHERS = initDispatchers();

    private static List<String> initLocations() {
        List<String> locations = new ArrayList<String>(4);
        locations.add("after-encoding");
        locations.add("before-login");
        locations.add("before-decoration");
        locations.add("before-dispatch");

        return Collections.unmodifiableList(locations);
    }

    private static List<String> initDispatchers() {
        List<String> dispatchers = new ArrayList<String>(4);
        dispatchers.add("REQUEST");
        dispatchers.add("INCLUDE");
        dispatchers.add("FORWARD");
        dispatchers.add("ERROR");

        return Collections.unmodifiableList(dispatchers);
    }

    public ServletFilterProperties() {
        this("MyServletFilter");
    }

    public ServletFilterProperties(String fqClassName) {
        super(fqClassName);
        put(DISPATCHERS, new ArrayList<String>());
        put(INIT_PARAMS, new HashMap<String, String>());

        //sane defaults
        setUrlPattern("/*");
        setLocation(ALLOWED_LOCATIONS.get(3));
        setWeight(100);
        setDescription("The " + getProperty(MODULE_NAME) + " Servlet Filter");
        setDescriptionKey(KEY_PREFIX + getProperty(MODULE_KEY) + ".description");
        addI18nProperty(getProperty(DESCRIPTION_KEY), getProperty(DESCRIPTION));

        setNameKey(KEY_PREFIX + getProperty(MODULE_KEY) + ".name");
        addI18nProperty(getProperty(NAME_KEY), getProperty(MODULE_NAME));
    }

    public void setLocation(String location) {
        setProperty(LOCATION, location);
    }

    public void setWeight(int weight) {
        setProperty(WEIGHT, Integer.toString(weight));
    }

    public void setUrlPattern(String pattern) {
        setProperty(URL_PATTERN, pattern);
    }

    public void setDispatchers(List<String> dispatchers) {
        put(DISPATCHERS, dispatchers);
    }

    @SuppressWarnings(value = "unchecked")
    public void addDispatcher(String dispatcher) {
        List<String> dispatchers = (List<String>) get(DISPATCHERS);
        if (dispatchers == null) {
            dispatchers = new ArrayList<String>();
            setDispatchers(dispatchers);
        }

        dispatchers.add(dispatcher);
    }

    public void setInitParams(Map<String, String> params) {
        put(INIT_PARAMS, params);
    }

    @SuppressWarnings(value = "unchecked")
    public void addInitParam(String name, String value) {
        Map<String, String> params = (Map<String, String>) get(INIT_PARAMS);
        if (params == null) {
            params = new HashMap<String, String>();
            setInitParams(params);
        }

        params.put(name, value);
    }

    public List<String> allowedLocations() {
        return ALLOWED_LOCATIONS;
    }

    public List<String> allowedDispatchers() {
        return ALLOWED_DISPATCHERS;
    }
}
