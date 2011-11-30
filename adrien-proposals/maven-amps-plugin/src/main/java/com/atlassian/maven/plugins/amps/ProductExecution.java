package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.product.ProductHandler;
import com.google.common.base.Preconditions;

/**
 * The execution context for a product
 */
public class ProductExecution
{
    private final Product product;
    private final ProductHandler productHandler;

    /**
     * Create a pair {product,handler}
     * @param product the product, never null
     * @param productHandler the handler, never null
     */
    public ProductExecution(Product product, ProductHandler productHandler)
    {
        Preconditions.checkArgument(product != null, "Can't instanciate a ProductExecution with no product");
        Preconditions.checkArgument(productHandler != null, "Can't instanciate a ProductExecution with no handler");

        this.product = product;
        this.productHandler = productHandler;
    }

    /**
     * @return the product handler, never null
     */
    public ProductHandler getProductHandler()
    {
        return productHandler;
    }

    /**
     * @return the product, never null
     */
    public Product getProduct()
    {
        return product;
    }

    @Override
    public String toString()
    {
        return "ProductExecution [product=" + product + ", productHandler=" + productHandler + "]";
    }


}
