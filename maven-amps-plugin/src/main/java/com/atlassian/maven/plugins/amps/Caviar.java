package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;


public class Caviar extends Product
{
    { id = ProductHandlerFactory.CAVIAR; }

    @Override
    public String toString()
    {
        return "Caviar " + id + " [instanceId=" + instanceId + ", localhost:" + httpPort + contextPath + "]";
    }

}
