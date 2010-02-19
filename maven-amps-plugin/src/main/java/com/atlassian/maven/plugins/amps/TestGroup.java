package com.atlassian.maven.plugins.amps;

import java.util.List;

public class TestGroup
{
    private String id;
    private List<String> products;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public List<String> getProductIds()
    {
        return products;
    }

    public void setProductIds(List<String> products)
    {
        this.products = products;
    }
}
