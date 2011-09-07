package com.atlassian.plugins.codegen.modules.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @since 3.5
 */
public class Label {
    private String key;
    private String value;
    private Map<String,String> params;

    public Label(String key,String value) {
        this.key = key;
        this.value = value;
        this.params = new HashMap<String, String>();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, String> getParams() {
        return Collections.unmodifiableMap(params);
    }

    public void addParam(String value) {
        String paramName = "param" + params.size();
        params.put(paramName,value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
