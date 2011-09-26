package com.atlassian.plugins.codegen.modules.common.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @since 3.5
 */
public class WebResourceTransformation
{
    private String extension;
    private List<String> transformerKeys;

    public WebResourceTransformation(String extension)
    {
        this.extension = extension;
        this.transformerKeys = new ArrayList<String>();
    }

    public String getExtension()
    {
        return extension;
    }

    public void setExtension(String extension)
    {
        this.extension = extension;
    }

    public void addTransformerKey(String key)
    {
        transformerKeys.add(key);
    }

    public List<String> getTransformerKeys()
    {
        return Collections.unmodifiableList(transformerKeys);
    }

    public void setTransformerKeys(List<String> keys)
    {
        transformerKeys = keys;
    }
}
