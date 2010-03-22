package com.atlassian.maven.plugins.amps;

import java.util.List;

/**
 * A group of tests to run for the given product identifiers
 *
 * @since 3.1
 */
public class TestGroup
{
    private String id;
    private List<String> products;
    private String include;

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


    public String getInclude()
    {
        return include;
    }

    /**
     * @param include The include file pattern
     */
    public void setInclude(String include)
    {
        this.include = include;
    }
}
