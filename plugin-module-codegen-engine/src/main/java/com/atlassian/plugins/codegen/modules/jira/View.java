package com.atlassian.plugins.codegen.modules.jira;

/**
 * @since version
 */
public class View {
    private String name;
    private String path;

    public View(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
