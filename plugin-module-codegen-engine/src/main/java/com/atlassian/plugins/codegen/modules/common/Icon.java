package com.atlassian.plugins.codegen.modules.common;

/**
 * @since 3.5
 */
public class Icon {

    private int width;
    private int height;
    private Link link;

    public Icon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }
}
