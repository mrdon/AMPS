package com.atlassian.plugins.codegen.modules.common;

/**
 * @since 3.5
 */
public class Link {
    private String linkId;
    private String value;

    public Link(String value) {
        this.value = value;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
