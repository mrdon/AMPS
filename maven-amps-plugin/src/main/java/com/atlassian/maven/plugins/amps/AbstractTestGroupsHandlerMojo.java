package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.product.ProductHandler;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import com.atlassian.maven.plugins.amps.product.studio.StudioProductHandler;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.apache.maven.plugin.MojoExecutionException;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractTestGroupsHandlerMojo extends AbstractProductHandlerMojo
{
    protected static final String NO_TEST_GROUP = "__no_test_group__";

    /**
     * The list of configured test groups
     */
    @MojoParameter
    private List<TestGroup> testGroups = new ArrayList<TestGroup>();

    protected final List<TestGroup> getTestGroups()
    {
        return testGroups;
    }

    protected final List<ProductExecution> getTestGroupProductExecutions(String testGroupId) throws MojoExecutionException
    {
        // Create a container object to hold product-related stuff
        List<ProductExecution> products = new ArrayList<ProductExecution>();
        int dupCounter = 0;
        Set<String> uniqueProductIds = new HashSet<String>();
        Map<String, Product> productContexts = getProductContexts(getMavenGoals());
        for (String productId : getTestGroupProductIds(testGroupId))
        {
            Product ctx = productContexts.get(productId);
            if (ctx == null)
            {
                throw new MojoExecutionException("The test group '" + testGroupId + "' refers to a product '" + productId
                    + "' that doesn't have an associated <product> configuration.");
            }
            ProductHandler productHandler = createProductHandler(ctx.getId());

            // Give unique ids to duplicate product instances
            if (uniqueProductIds.contains(productId))
            {
                ctx.setInstanceId(productId + "-" + dupCounter++);
            }
            else
            {
                uniqueProductIds.add(productId);
            }
            products.add(new ProductExecution(ctx, productHandler));
        }

        return products;
    }

    /**
     * Returns the products in the test group:
     * <ul>
     * <li>If a {@literal <testGroup>} is defined, all the products of this test group</li>
     * <li>If testGroupId is __no_test_group__, adds it</li>
     * <li>If testGroupId is a product instanceId, adds it</li>
     * </ul>
     */
    private List<String> getTestGroupProductIds(String testGroupId) throws MojoExecutionException
    {
        List<String> productIds = new ArrayList<String>();
        if (NO_TEST_GROUP.equals(testGroupId))
        {
            productIds.add(getProductId());
        }

        for (TestGroup group : testGroups)
        {
            if (group.getId().equals(testGroupId))
            {
                productIds.addAll(group.getProductIds());
            }
        }
        if (ProductHandlerFactory.getIds().contains(testGroupId) && !productIds.contains(testGroupId))
        {
            productIds.add(testGroupId);
        }

        if (productIds.isEmpty())
        {
            List<String> validTestGroups = new ArrayList<String>();
            for (TestGroup group: testGroups)
            {
                validTestGroups.add(group.getId());
            }
            throw new MojoExecutionException("Unknown test group ID: " + testGroupId
                + " Detected IDs: " + Arrays.toString(validTestGroups.toArray()));
        }

        return productIds;
    }

    

    /**
     * If there is any Studio instance, returns a list with all products requested by this instance
     * @param productExecutions the current list of products to run
     * @param goals
     * @return the complete list of products to run
     * @throws MojoExecutionException
     */
    protected List<ProductExecution> includeStudioDependentProducts(final List<ProductExecution> productExecutions, final MavenGoals goals)
            throws MojoExecutionException
    {
        // If one of the products is Studio, ask him/her which other products he/she wants to run
        Iterator<ProductExecution> studioExecutions = getStudioExecutions(productExecutions);
        if (!studioExecutions.hasNext())
        {
            return productExecutions;
        }
        
        // We have studio execution(s), so we need to add all products requested by Studio
        List<ProductExecution> productExecutionsIncludingStudio = Lists.newArrayList(productExecutions);
        while (studioExecutions.hasNext())
        {
            ProductExecution studioExecution = studioExecutions.next();
            Product studioProduct = studioExecution.getProduct();
            StudioProductHandler studioProductHandler = (StudioProductHandler) studioExecution.getProductHandler();
            
            // Ask the Studio Product Handler the list of required products
            final List<String> dependantProductIds = studioProductHandler.getDependantInstances(studioProduct);
            
            // Fetch the products
            List<ProductExecution> dependantProducts = Lists.newArrayList();
            Map<String, Product> allContexts = getProductContexts(goals);
            for (String instanceId : dependantProductIds)
            {
                Product product = allContexts.get(instanceId);
                ProductHandler handler;
                if (product == null)
                {
                    handler = createProductHandler(instanceId);
                    product = createProductContext(instanceId, instanceId, handler);
                }
                else
                {
                    handler = createProductHandler(product.getId());
                }

                dependantProducts.add(new ProductExecution(product, handler));
            }
            
            // Submit those products to StudioProductHanlder for configuration
            studioProductHandler.configure(studioProduct, dependantProducts);
            
            // If the user passes some system properties, we don't run some products
            // We'll keep them configured and available in StudioProperties
            Set<String> exclusions = studioProductHandler.getExcludedInstances(studioProduct);
            if (exclusions != null)
            {
                Iterator<ProductExecution> iterator = dependantProducts.iterator();
                while (iterator.hasNext())
                {
                    String executedInstance = iterator.next().getProduct().getInstanceId();
                    if (exclusions.contains(executedInstance))
                    {
                        iterator.remove();
                    }
                }
            }
                    
            // Add everyone at the end of the list of products to execute. We don't check for duplicates, users shouldn't add studio products
            // to test groups, especially if they already have a Studio.
            productExecutionsIncludingStudio.addAll(dependantProducts);
        }
        
        return productExecutionsIncludingStudio;
    }

    private Product createProductContext(String productNickname, String instanceId, ProductHandler handler) throws MojoExecutionException
    {
        getLog().info("Studio (instanceId=%s): No product with instanceId=%s is defined in the pom. Using a default product.");
        Product product;
        product = createDefaultProductContext();
        product.setId(productNickname);
        product.setInstanceId(instanceId);
        setDefaultValues(product, handler);
        return product;
    }

    private Iterator<ProductExecution> getStudioExecutions(final List<ProductExecution> productExecutions)
    {
        return Iterables.filter(productExecutions, new Predicate<ProductExecution>(){

            @Override
            public boolean apply(ProductExecution input)
            {
                return input.getProductHandler() instanceof StudioProductHandler;
            }}).iterator();
    }

}
