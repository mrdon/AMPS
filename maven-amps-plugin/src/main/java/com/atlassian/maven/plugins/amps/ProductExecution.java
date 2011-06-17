package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.product.ProductHandler;

/**
 * The execution context for a product
 */
class ProductExecution
{
    private final Product product;
    private final ProductHandler productHandler;

    public ProductExecution(Product product, ProductHandler productHandler)
    {
        this.product = product;
        this.productHandler = productHandler;
    }

    public ProductHandler getProductHandler()
    {
        return productHandler;
    }

    public Product getProduct()
    {
        return product;
    }
}
