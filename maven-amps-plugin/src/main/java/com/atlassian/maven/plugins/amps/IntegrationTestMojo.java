package com.atlassian.maven.plugins.amps;

import static com.google.common.collect.Iterables.transform;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.jfrog.maven.annomojo.annotations.MojoComponent;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;
import org.jfrog.maven.annomojo.annotations.MojoRequiresDependencyResolution;

import com.atlassian.maven.plugins.amps.product.ProductHandler;
import com.atlassian.util.concurrent.AsyncCompleter;
import com.atlassian.util.concurrent.ExceptionPolicy;
import com.google.common.base.Function;

/**
 * Run the integration tests against the webapp
 */
@MojoGoal("integration-test")
@MojoRequiresDependencyResolution("test")
public class IntegrationTestMojo extends AbstractTestGroupsHandlerMojo
{
    /**
     * Pattern for to use to find integration tests.  Only used if no test groups are defined.
     */
    @MojoParameter(expression = "${functional.test.pattern}")
    private String functionalTestPattern = "it/**";

    /**
     * The directory containing generated test classes of the project being tested.
     */
    @MojoParameter(expression = "${project.build.testOutputDirectory}", required = true)
    private File testClassesDirectory;

    /**
     * A comma separated list of test groups to run.  If not specified, all
     * test groups are run.
     */
    @MojoParameter(expression = "${testGroups}")
    private String configuredTestGroupsToRun;
    
    /**
     * Whether the reference application will not be started or not
     */
    @MojoParameter(expression = "${no.webapp}", defaultValue = "false")
    private boolean noWebapp = false;

    @MojoComponent
    private ArtifactHandlerManager artifactHandlerManager;

    @MojoParameter(expression="${maven.test.skip}", defaultValue = "false")
    private boolean testsSkip = false;

    @MojoParameter(expression="${skipTests}", defaultValue = "false")
    private boolean skipTests = false;
    
    /**
     * Skip the integration tests along with any product startups
     */
    @MojoParameter(expression="${skipITs}", defaultValue = "false")
    private boolean skipITs = false;

    protected void doExecute() throws MojoExecutionException
    {
        final MavenProject project = getMavenContext().getProject();

        // workaround for MNG-1682/MNG-2426: force maven to install artifact using the "jar" handler
        project.getArtifact().setArtifactHandler(artifactHandlerManager.getArtifactHandler("jar"));

        if (!new File(testClassesDirectory, "it").exists())
        {
            getLog().info("No integration tests found");
            return;
        }

        if (skipTests || testsSkip || skipITs)
        {
            getLog().info("Integration tests skipped");
            return;
        }

        final MavenGoals goals = getMavenGoals();
        final String pluginJar = targetDirectory.getAbsolutePath() + "/" + finalName + ".jar";

        final Set<String> configuredTestGroupIds = getTestGroupIds();
        if (configuredTestGroupIds.isEmpty())
        {
            runTestsForTestGroup(NO_TEST_GROUP, goals, pluginJar, copy(systemPropertyVariables));
        }
        else if (configuredTestGroupsToRun != null)
        {
            String[] testGroupIdsToRun = configuredTestGroupsToRun.split(",");
            
            // now run the tests
            for (String testGroupId : testGroupIdsToRun)
            {
                if (!configuredTestGroupIds.contains(testGroupId))
                {
                    getLog().warn("Test group " + testGroupId + " does not exist");
                }
                else
                {
                    runTestsForTestGroup(testGroupId, goals, pluginJar, copy(systemPropertyVariables));
                }
            }
        }
        else
        {
            for (String testGroupId : configuredTestGroupIds)
            {
                runTestsForTestGroup(testGroupId, goals, pluginJar, copy(systemPropertyVariables));
            }
        }
    }

    private Map<String,Object> copy(Map<String,Object> systemPropertyVariables)
    {
        return new HashMap<String,Object>(systemPropertyVariables);
    }

    /**
     * Returns product-specific properties to pass to the container during
     * integration testing. Default implementation does nothing.
     * @param product the {@code Product} object to use
     * @return a {@code Map} of properties to add to the system properties passed
     * to the container
     */
    protected Map<String, String> getProductFunctionalTestProperties(Product product)
    {
        return Collections.emptyMap();
    }

    private Set<String> getTestGroupIds() throws MojoExecutionException
    {
        Set<String> ids = new HashSet<String>();

        //ids.addAll(ProductHandlerFactory.getIds());
        for (TestGroup group : getTestGroups())
        {
            ids.add(group.getId());
        }

        return ids;
    }

    private void runTestsForTestGroup(String testGroupId, MavenGoals goals, String pluginJar, Map<String,Object> systemProperties) throws MojoExecutionException
    {
        List<String> includes = getIncludesForTestGroup(testGroupId);
        List<String> excludes = getExcludesForTestGroup(testGroupId);

        List<ProductExecution> productExecutions = getTestGroupProductExecutions(testGroupId);

        ExecutorService executor = Executors.newFixedThreadPool(productExecutions.size());
        AsyncCompleter completer = new AsyncCompleter.Builder(executor).handleExceptions(ExceptionPolicy.Policies.THROW).build();
        
        // Install the plugin in each product and start it
        for (Map<String, Object> productProperties : completer.invokeAll(transform(productExecutions, productStarter(pluginJar))))
        {
            systemProperties.putAll(productProperties);
        }
        if (productExecutions.size() == 1)
        {
            Product product = productExecutions.get(0).getProduct();
            systemProperties.put("http.port", systemProperties.get("http." + product.getInstanceId() + ".port"));
            systemProperties.put("context.path", product.getContextPath());
        }
        systemProperties.put("testGroup", testGroupId);
        systemProperties.putAll(getTestGroupSystemProperties(testGroupId));

        // Actually run the tests
        goals.runTests("group-" + testGroupId, containerId, includes, excludes, systemProperties, targetDirectory);

        // Shut all products down
        if (!noWebapp)
        {
            for (Void _ : completer.invokeAll(transform(productExecutions, productStopper()))) {}
        }
        executor.shutdown();
    }

    private Function<ProductExecution, Callable<Map<String, Object>>> productStarter(final String pluginJar)
    {
        return new Function<ProductExecution, Callable<Map<String,Object>>>()
        {
            @Override
            public Callable<Map<String, Object>> apply(ProductExecution productExecution)
            {
                return new ProductStarter(pluginJar, productExecution);
            }
        };
    }
    
    private final class ProductStarter implements Callable<Map<String, Object>>
    {
        private final String pluginJar;
        private final ProductExecution productExecution;

        public ProductStarter(String pluginJar, ProductExecution productExecution)
        {
            this.pluginJar = pluginJar;
            this.productExecution = productExecution;
        }

        @Override
        public Map<String, Object> call() throws Exception
        {
            Map<String, Object> properties = new HashMap<String, Object>();
            ProductHandler productHandler = productExecution.getProductHandler();
            Product product = productExecution.getProduct();
            product.setInstallPlugin(installPlugin);

            int actualHttpPort = 0;
            if (!noWebapp)
            {
                actualHttpPort = productHandler.start(product);
            }

            String baseUrl = MavenGoals.getBaseUrl(product.getServer(), actualHttpPort, product.getContextPath());
            // hard coded system properties...
            properties.put("http." + product.getInstanceId() + ".port", String.valueOf(actualHttpPort));
            properties.put("context." + product.getInstanceId() + ".path", product.getContextPath());
            properties.put("http." + product.getInstanceId() + ".url", MavenGoals.getBaseUrl(product.getServer(), actualHttpPort, product.getContextPath()));

            properties.put("baseurl." + product.getInstanceId(), baseUrl);
            properties.put("plugin.jar", pluginJar);

            properties.put("baseurl", baseUrl);
            
            properties.put("homedir." + product.getInstanceId(), productHandler.getHomeDirectory(product).getAbsolutePath());
            if (!properties.containsKey("homedir"))
            {
                properties.put("homedir", productHandler.getHomeDirectory(product).getAbsolutePath());
            }
            
            properties.putAll(getProductFunctionalTestProperties(product));
            return properties;
        }
    }
    
    private Function<ProductExecution, Callable<Void>> productStopper()
    {
        return ProductStopper.INSTANCE;
    }
    
    enum ProductStopper implements Function<ProductExecution, Callable<Void>>
    {
        INSTANCE;

        @Override
        public Callable<Void> apply(final ProductExecution productExecution)
        {
            return new Callable<Void>()
            {
                @Override
                public Void call() throws Exception
                {
                    productExecution.getProductHandler().stop(productExecution.getProduct());
                    return null;
                }
            };
        }
    }

    private Map<String, String> getTestGroupSystemProperties(String testGroupId)
    {
        if (NO_TEST_GROUP.equals(testGroupId))
        {
            return Collections.emptyMap();
        }

        for (TestGroup group : getTestGroups())
        {
            if (group.getId().equals(testGroupId))
            {
                return group.getSystemProperties();
            }
        }
        return Collections.emptyMap();
    }

    private List<String> getIncludesForTestGroup(String testGroupId)
    {
        if (NO_TEST_GROUP.equals(testGroupId))
        {
            return Collections.singletonList(functionalTestPattern);
        }
        else
        {
            for (TestGroup group : getTestGroups())
            {
                if (group.getId().equals(testGroupId))
                {
                    return group.getIncludes();
                }
            }
        }
        return Collections.singletonList(functionalTestPattern);
    }


    private List<String> getExcludesForTestGroup(String testGroupId)
    {
        if (NO_TEST_GROUP.equals(testGroupId))
        {
            return Collections.emptyList();
        }
        else
        {
            for (TestGroup group : getTestGroups())
            {
                if (group.getId().equals(testGroupId))
                {
                    return group.getExcludes();
                }
            }
        }
        return Collections.emptyList();
    }
}