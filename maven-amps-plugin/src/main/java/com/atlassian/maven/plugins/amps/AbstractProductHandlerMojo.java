package com.atlassian.maven.plugins.amps;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.atlassian.maven.plugins.amps.product.ProductHandler;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import com.atlassian.maven.plugins.amps.product.studio.StudioProductHandler;
import com.atlassian.maven.plugins.amps.util.ArtifactRetriever;
import com.atlassian.maven.plugins.amps.util.ProjectUtils;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.jfrog.maven.annomojo.annotations.MojoComponent;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

/**
 * Base class for webapp mojos
 */
public abstract class AbstractProductHandlerMojo extends AbstractProductHandlerAwareMojo {

    // ------ start inline product context

    private static final String DEFAULT_CONTAINER = "tomcat6x";
    private static final String TOMCAT5_CONTAINER = "tomcat5x";
    private static final String DEFAULT_SERVER;
    private static final String DEFAULT_PRODUCT_DATA_VERSION = "LATEST";
    private static final String DEFAULT_PDK_VERSION = "0.4";
    private static final String DEFAULT_WEB_CONSOLE_VERSION = "1.2.8";
    private static final String DEFAULT_FASTDEV_VERSION = "1.9";
    private static final String DEFAULT_DEV_TOOLBOX_VERSION = "1.0.3";

    /**
      * Default product startup timeout: three minutes
     */
    private static final int DEFAULT_PRODUCT_STARTUP_TIMEOUT = 1000 * 60 * 3;

    /**
      * Default product shutdown timeout: three minutes
      */
    private static final int DEFAULT_PRODUCT_SHUTDOWN_TIMEOUT = 1000 * 60 * 3;

    static
    {
        String localHostName = null;
        try
        {
            localHostName = InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e)
        {
            localHostName = "localhost";
        }
        DEFAULT_SERVER = localHostName;
    }

    /**
     * Container to run in
     */
    @MojoParameter(expression = "${container}")
    protected String containerId;

    /**
     * HTTP port for the servlet containers
     */
    @MojoParameter(expression = "${http.port}", defaultValue = "0")
    private int httpPort;

    /**
     * Application context path
     */
    @MojoParameter(expression = "${context.path}")
    protected String contextPath;

    /**
     * Application server
     */
    @MojoParameter(expression = "${server}")
    protected String server;

    /**
     * Webapp version
     */
    @MojoParameter(expression = "${product.version}")
    private String productVersion;

    /**
     * JVM arguments to pass to cargo
     */
    @MojoParameter(expression = "${jvmargs}")
    protected String jvmArgs;

    /**
     * Product startup timeout in milliseconds
     */
    @MojoParameter(expression = "${product.start.timeout}")
    private int startupTimeout;

    /**
     * Product shutdown timeout in milliseconds
     */
    @MojoParameter(expression = "${product.stop.timeout}")
    private int shutdownTimeout;

    /**
     * System systemProperties to pass to cargo
     *
     * @deprecated Since 3.2, use systemPropertyVariables instead
     */
    @MojoParameter
    @Deprecated
    protected Properties systemProperties = new Properties();

    /**
     * System Properties to pass to cargo using a more familiar syntax.
     *
     * @since 3.2
     */
    @MojoParameter
    protected Map<String, Object> systemPropertyVariables = new HashMap<String, Object>();


    /**
     * A log4j systemProperties file
     */
    @MojoParameter
    protected File log4jProperties;

    /**
     * The test resources version
     * @deprecated Since 3.0-beta2
     */
    @Deprecated
    @MojoParameter(expression = "${test.resources.version}")
    private String testResourcesVersion;

    /**
     * The test resources version
     */
    @MojoParameter(expression = "${product.data.version}", defaultValue = DEFAULT_PRODUCT_DATA_VERSION)
    private String productDataVersion;

    /**
     * The path to a custom test resources zip
     */
    @MojoParameter(expression = "${product.data.path}")
    private String productDataPath;

    /**
     * If FastDev should be enabled
     */
    @MojoParameter(expression = "${fastdev.enable}", defaultValue = "true")
    protected boolean enableFastdev;

    /**
     * The version of FastDev to bundle
     */
    @MojoParameter(expression = "${fastdev.version}", defaultValue = DEFAULT_FASTDEV_VERSION)
    protected String fastdevVersion;

    /**
     * If DevToolbox should be enabled
     */
    @MojoParameter(expression = "${devtoolbox.enable}", defaultValue = "true")
    protected boolean enableDevToolbox;

    /**
     * The version of DevToolbox to bundle
     */
    @MojoParameter(expression = "${devtoolbox.version}", defaultValue = DEFAULT_DEV_TOOLBOX_VERSION)
    protected String devToolboxVersion;

    @MojoParameter
    private List<ProductArtifact> pluginArtifacts = new ArrayList<ProductArtifact>();

    /**
     */
    @MojoParameter
    private List<ProductArtifact> libArtifacts = new ArrayList<ProductArtifact>();

    /**
     */
    @MojoParameter
    private List<ProductArtifact> bundledArtifacts = new ArrayList<ProductArtifact>();

    /**
     * SAL version
     * @deprecated Since 3.2, use {@link #pluginArtifacts} instead
     */
    @Deprecated
    @MojoParameter
    private String salVersion;

    /**
     * Atlassian Plugin Development Kit (PDK) version
     * @deprecated Since 3.2, use {@link #pluginArtifacts} instead
     */
    @Deprecated
    @MojoParameter(defaultValue = DEFAULT_PDK_VERSION)
    private String pdkVersion;

    /**
     * Atlassian REST module version
     * @deprecated Since 3.2, use {@link #pluginArtifacts} instead
     */
    @Deprecated
    @MojoParameter
    private String restVersion;


    /**
     * Felix OSGi web console version
     * @deprecated Since 3.2, use {@link #pluginArtifacts} instead
     */
    @Deprecated
    @MojoParameter(defaultValue =  DEFAULT_WEB_CONSOLE_VERSION)
    private String webConsoleVersion;

    // ---------------- end product context

    /**
     * Comma-delimited list of plugin artifacts in GROUP_ID:ARTIFACT_ID:VERSION form, where version can be
     * ommitted, defaulting to LATEST
     */
    @MojoParameter(expression = "${plugins}")
    private String pluginArtifactsString;

    /**
     * Comma-delimited list of lib artifacts in GROUP_ID:ARTIFACT_ID:VERSION form, where version can be
     * ommitted, defaulting to LATEST
     */
    @MojoParameter(expression = "${lib.plugins}")
    private String libArtifactsString;

    /**
     * Comma-delimited list of bundled plugin artifacts in GROUP_ID:ARTIFACT_ID:VERSION form, where version can be
     * ommitted, defaulting to LATEST
     */
    @MojoParameter(expression = "${bundled.plugins}")
    private String bundledArtifactsString;

    /**
     * The build directory
     */
    @MojoParameter(expression = "${project.build.directory}", required = true)
    protected File targetDirectory;

    /**
     * The jar name
     */
    @MojoParameter(expression = "${project.build.finalName}", required = true)
    protected String finalName;

    /**
     * If the plugin and optionally its test plugin should be installed
     */
    @MojoParameter (expression = "${install.plugin}", defaultValue = "true")
    protected boolean installPlugin;

    /**
     * The artifact resolver is used to dynamically resolve JARs that have to be in the embedded
     * container's classpaths. Another solution would have been to statitically define them a
     * dependencies in the plugin's POM. Resolving them in a dynamic manner is much better as only
     * the required JARs for the defined embedded container are downloaded.
     */
    @MojoComponent
    protected ArtifactResolver artifactResolver;

    /**
     * The local Maven repository. This is used by the artifact resolver to download resolved
     * JARs and put them in the local repository so that they won't have to be fetched again next
     * time the plugin is executed.
     */
    @MojoParameter(expression = "${localRepository}")
    protected ArtifactRepository localRepository;


    /**
     * The remote Maven repositories used by the artifact resolver to look for JARs.
     */
    @MojoParameter(expression = "${project.remoteArtifactRepositories}")
    protected List repositories;

    /**
     * The artifact factory is used to create valid Maven
     * {@link org.apache.maven.artifact.Artifact} objects. This is used to pass Maven artifacts to
     * the artifact resolver so that it can download the required JARs to put in the embedded
     * container's classpaths.
     */
    @MojoComponent
    protected ArtifactFactory artifactFactory;

    /**
     * File the container logging output will be sent to.
     */
    @MojoParameter
    private String output;

    /**
     * Start the products in parallel (TestGroups and Studio).
     */
    @MojoParameter (expression = "${parallel}", defaultValue = "false")
    protected boolean parallel;


    protected Product createDefaultProductContext() throws MojoExecutionException
    {
        Product ctx = new Product();
        ctx.setId(getProductId());
        ctx.setContainerId(containerId);
        ctx.setServer(server);
        ctx.setContextPath(contextPath);
        ctx.setJvmArgs(jvmArgs);
        ctx.setStartupTimeout(startupTimeout);
        ctx.setShutdownTimeout(shutdownTimeout);

        // If they aren't defined, define those system properties. They will override the product
        // handler's properties.
        Map<String, Object> properties = new HashMap<String, Object>(systemPropertyVariables);
        setDefaultSystemProperty(properties, "atlassian.dev.mode", "true");
        setDefaultSystemProperty(properties, "java.awt.headless", "true");
        setDefaultSystemProperty(properties, "plugin.resource.directories", buildResourcesList());
        setDefaultSystemProperty(properties, "plugin.root.directories", buildRootProperty());

        ctx.setSystemPropertyVariables(properties);
        ctx.setBundledArtifacts(bundledArtifacts);
        ctx.setLibArtifacts(libArtifacts);
        ctx.setPluginArtifacts(pluginArtifacts);
        ctx.setLog4jProperties(log4jProperties);
        ctx.setHttpPort(httpPort);

        ctx.setVersion(productVersion);
        ctx.setDataVersion(productDataVersion);
        ctx.setDataPath(productDataPath);

        // continue to have these work for now
        ctx.setRestVersion(restVersion);
        ctx.setSalVersion(salVersion);
        ctx.setPdkVersion(pdkVersion);
        ctx.setWebConsoleVersion(webConsoleVersion);

        ctx.setEnableFastdev(enableFastdev);
        ctx.setFastdevVersion(fastdevVersion);

        ctx.setEnableDevToolbox(enableDevToolbox);
        ctx.setDevToolboxVersion(devToolboxVersion);

        ctx.setHttpPort(httpPort);
        return ctx;
    }

    /**
     * @return a comma-separated list of resource directories.  If a test plugin is detected, the
     * test resources directories are included as well.
     */
    private String buildResourcesList()
    {
        // collect all resource directories and make them available for
        // on-the-fly reloading
        StringBuilder resourceProp = new StringBuilder();
        MavenProject mavenProject = getMavenContext().getProject();
        @SuppressWarnings("unchecked") List<Resource> resList = mavenProject.getResources();
        for (int i = 0; i < resList.size(); i++) {
            resourceProp.append(resList.get(i).getDirectory());
            if (i + 1 != resList.size()) {
                resourceProp.append(",");
            }
        }

        if (ProjectUtils.shouldDeployTestJar(getMavenContext()))
        {
            @SuppressWarnings("unchecked") List<Resource> testResList = mavenProject.getTestResources();
            for (int i = 0; i < testResList.size(); i++) {
                if (i == 0 && resourceProp.length() > 0)
                {
                    resourceProp.append(",");
                }
                resourceProp.append(testResList.get(i).getDirectory());
                if (i + 1 != testResList.size()) {
                    resourceProp.append(",");
                }
            }
        }
        return resourceProp.toString();
    }

    /**
     * @return the path of the project root, for the <tt>plugin.root.directories</tt> system property.
     *
     * @since 3.6
     */
    private String buildRootProperty()
    {
        MavenProject mavenProject = getMavenContext().getProject();
        return mavenProject.getBasedir().getPath();
    }

    private static void setDefaultSystemProperty(final Map<String,Object> props, final String key, final String value)
    {
        if (!props.containsKey(key))
        {
            props.put(key, System.getProperty(key, value));
        }
    }

    /**
     * Set the default values for the product
     * @param product the product
     * @param handler the product handler associated to the product
     */
    protected void setDefaultValues(Product product, ProductHandler handler)
    {
        product.setInstanceId(getProductInstanceId(product));

        // If it's a Studio product, some defaults are different (ex: context path for Confluence is /wiki)
        StudioProductHandler.setDefaultValues(getMavenContext(), product);

        //Apply the common default values
        String dversion = System.getProperty("product.data.version", product.getDataVersion());
        String pversion = System.getProperty("product.version", product.getVersion());
        String dpath = System.getProperty("product.data.path", product.getDataPath());

        product.setDataPath(dpath);
        product.setDataVersion(dversion);
        product.setVersion(pversion);
        product.setArtifactRetriever(new ArtifactRetriever(artifactResolver, artifactFactory, localRepository, repositories));

        if (product.getContainerId() == null)
        {
            if (ProductHandlerFactory.CAVIAR.equals(product.getId()))
            {
                product.setContainerId(TOMCAT5_CONTAINER);
            }
            else
            {
                product.setContainerId(DEFAULT_CONTAINER);
            }
        }

        if (product.getServer() == null)
        {
            product.setServer(DEFAULT_SERVER);
        }

        if (product.getDataVersion() == null)
        {
            product.setDataVersion(DEFAULT_PRODUCT_DATA_VERSION);
        }

        if (product.getPdkVersion() == null)
        {
            product.setPdkVersion(DEFAULT_PDK_VERSION);
        }

        if (product.getWebConsoleVersion() == null)
        {
            product.setWebConsoleVersion(DEFAULT_WEB_CONSOLE_VERSION);
        }

        if (product.isEnableFastdev() == null)
        {
            product.setEnableFastdev(true);
        }

        if (product.getFastdevVersion() == null)
        {
            product.setFastdevVersion(DEFAULT_FASTDEV_VERSION);
        }

        if (product.isEnableDevToolbox() == null)
        {
            product.setEnableDevToolbox(true);
        }

        if (product.getDevToolboxVersion() == null)
        {
            product.setDevToolboxVersion(DEFAULT_DEV_TOOLBOX_VERSION);
        }

        if (product.getOutput() == null)
        {
            product.setOutput(output);
        }

        if (product.getStartupTimeout() <= 0)
        {
            product.setStartupTimeout(DEFAULT_PRODUCT_STARTUP_TIMEOUT);
        }

        if (product.getShutdownTimeout() <= 0)
        {
            product.setShutdownTimeout(DEFAULT_PRODUCT_SHUTDOWN_TIMEOUT);
        }

        if (product.getHttpPort() == 0)
        {
            product.setHttpPort(handler.getDefaultHttpPort());
        }

        if (product.getVersion() == null)
        {
            product.setVersion("RELEASE");
        }

        if (product.getContextPath() == null)
        {
            product.setContextPath("/" + handler.getId());
        }
    }

    private List<ProductArtifact> stringToArtifactList(String val, List<ProductArtifact> artifacts)
    {
        if (val == null || val.trim().length() == 0)
        {
            return artifacts;
        }

        for (String ptn : val.split(","))
        {
            String[] items = ptn.split(":");
            if (items.length < 2 || items.length > 3)
            {
                throw new IllegalArgumentException("Invalid artifact pattern: " + ptn);
            }
            String groupId = items[0];
            String artifactId = items[1];
            String version = (items.length == 3 ? items[2] : "LATEST");
            artifacts.add(new ProductArtifact(groupId, artifactId, version));
        }
        return artifacts;
    }

    @Override
    public final void execute() throws MojoExecutionException, MojoFailureException
    {
        stringToArtifactList(pluginArtifactsString, pluginArtifacts);
        stringToArtifactList(libArtifactsString, libArtifacts);
        stringToArtifactList(bundledArtifactsString, bundledArtifacts);
        systemPropertyVariables.putAll((Map) systemProperties);

        detectDeprecatedVersionOverrides();

        doExecute();
    }

    private void detectDeprecatedVersionOverrides()
    {
        Properties props = getMavenContext().getProject().getProperties();
        for (String deprecatedProperty : new String[] {"sal.version", "rest.version", "web.console.version", "pdk.version"})
        {
            if (props.containsKey(deprecatedProperty))
            {
                getLog().warn("The property '" + deprecatedProperty + "' is no longer usable to override the related bundled plugin." +
                        "  Use <pluginArtifacts> or <libArtifacts> to explicitly override bundled plugins and libraries, respectively.");
            }
        }
    }

    /**
     * Returns the Product objects that are defined in our maven-amps-plugins object:
     * <ul>
     * <li>Reads the {@literal <products>} tag</li>
     * <li>Defaults the values</li>
     * </ul>
     * So the method looks short but it's quite central in the initialisation of products.
     */
    protected Map<String, Product> getProductContexts(MavenGoals goals) throws MojoExecutionException
    {
        Map<String, Product> productMap = new HashMap<String, Product>();

        // Products in the <products> tag inherit from the upper settings, e.g. when there's a <httpPort> tag for for all products
        makeProductsInheritDefaultConfiguration(products, productMap);

        for (Product ctx : productMap.values())
        {
            ProductHandler handler = ProductHandlerFactory.create(ctx.getId(), getMavenContext(), goals);
            setDefaultValues(ctx, handler);
        }
        return productMap;
    }

    /**
     * Puts the list of {@literal <products>} in productMap:
     * <ul>
     * <li>The {@literal <product>} from the maven-amps-plugin configuration (if missing, RefApp is used)</li>
     * <li>The {@literal <products>} from the maven-amps-plugin configuration</li>
     * </ul>
     */
    void makeProductsInheritDefaultConfiguration(List<Product> products, Map<String, Product> productMap) throws MojoExecutionException
    {
        Product defaultProduct = createDefaultProductContext();
        productMap.put(getProductId(), defaultProduct);
        if (!products.isEmpty())
        {
            for (Product product : products)
            {
                Product processedProduct = product.merge(defaultProduct);
                if (ProductHandlerFactory.STUDIO_CROWD.equals(processedProduct.getId()))
                {
                    // This is a temporary fix for StudioCrowd - it requires atlassian.dev.mode=false - see AMPS-556
                    processedProduct.getSystemPropertyVariables().put("atlassian.dev.mode", "false");
                }
                String instanceId = getProductInstanceId(processedProduct);
                productMap.put(instanceId, processedProduct);
            }
        }
    }

    private String getProductInstanceId(Product processedProduct)
    {
        return processedProduct.getInstanceId() == null ? processedProduct.getId() : processedProduct.getInstanceId();
    }


    private Product createProductContext(String productNickname, String instanceId, ProductHandler handler) throws MojoExecutionException
    {
        getLog().info(String.format("Studio (instanceId=%s): No product with name %s is defined in the pom. Using a default product.", instanceId, productNickname));
        Product product;
        product = createDefaultProductContext();
        product.setId(productNickname);
        product.setInstanceId(instanceId);
        setDefaultValues(product, handler);
        if (ProductHandlerFactory.STUDIO_CROWD.equals(product.getId()))
        {
            // This is a temporary fix for StudioCrowd - it requires atlassian.dev.mode=false - see AMPS-556
            product.getSystemPropertyVariables().put("atlassian.dev.mode", "false");
        }
        return product;
    }

    /**
     * Attempts to stop all products. Returns after the timeout or as soon as all products
     * are shut down.
     */
    protected void stopProducts(List<ProductExecution> productExecutions) throws MojoExecutionException
    {
        ExecutorService executor = Executors.newFixedThreadPool(productExecutions.size());
        try
        {
            long before = System.nanoTime();
            for (final ProductExecution execution : Iterables.reverse(productExecutions))
            {
                final Product product = execution.getProduct();
                final ProductHandler productHandler = execution.getProductHandler();

                Future<?> task = executor.submit(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        getLog().info(product.getInstanceId() + ": Shutting down");
                        try
                        {
                            productHandler.stop(product);
                        }
                        catch (MojoExecutionException e)
                        {
                            getLog().error("Exception while trying to stop " + product.getInstanceId(), e);
                        }
                    }
                });

                boolean successful = true;
                try
                {
                    task.get(product.getShutdownTimeout(), TimeUnit.MILLISECONDS);
                }
                catch (TimeoutException e)
                {
                    getLog().info(product.getInstanceId() + " shutdown: Didn't return in time");
                    successful = false;
                    task.cancel(true);
                }
            }
            long after = System.nanoTime();
            getLog().info("amps:stop in " + TimeUnit.NANOSECONDS.toSeconds(after - before) + "s");
        }
        catch (InterruptedException e1)
        {
            Thread.currentThread().interrupt();
        }
        catch (ExecutionException e)
        {
            throw new MojoExecutionException("Exception while stopping the products", e);
        }

        // If products were launched in parallel, check they are stopped: CodeHaus Cargo returns before
        // products are down.
        if (parallel)
        {
            waitForProducts(productExecutions, false);
        }
    }


    /**
     * Waits until all products are running or stopped
     * @param startingUp true if starting up the products, false if shutting down.
     */
    protected void waitForProducts(List<ProductExecution> productExecutions, boolean startingUp) throws MojoExecutionException
    {
        for (ProductExecution productExecution : productExecutions)
        {
            pingRepeatedly(productExecution.getProduct(), startingUp);
        }
    }

    /**
     * Ping the product until it's up or stopped
     * @param startingUp true if applications are expected to be up; false if applications are expected to be brought down
     * @throws MojoExecutionException if the product didn't have the expected behaviour beofre the timeout
     */
    private void pingRepeatedly(Product product, boolean startingUp) throws MojoExecutionException
    {
        if (product.getHttpPort() != 0)
        {
            String url = "http://" + product.getServer() + ":" + product.getHttpPort();
            if (StringUtils.isNotBlank(product.getContextPath()))
            {
                url = url + product.getContextPath();
            }

            int timeout = startingUp ? product.getStartupTimeout() : product.getShutdownTimeout();
            final long end = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeout);
            boolean interrupted = false;
            boolean success = false;
            String lastMessage = "";

            // keep retrieving from the url until a good response is returned, under a time limit.
            while (!success && !interrupted && System.nanoTime() < end)
            {
                HttpURLConnection connection = null;
                try
                {
                    URL urlToPing = new URL(url);
                    connection = (HttpURLConnection) urlToPing.openConnection();
                    int response = connection.getResponseCode();
                    // Tomcat returns 404 until the webapp is up
                    lastMessage = "Last response code is " + response;
                    if (startingUp)
                    {
                        success = response < 400;
                    }
                    else
                    {
                        success = response >= 400;
                    }
                }
                catch (IOException e)
                {
                    lastMessage = e.getMessage();
                    success = !startingUp;
                }
                finally
                {
                    if (connection != null)
                    {
                        try
                        {
                            connection.getInputStream().close();
                        }
                        catch (IOException e)
                        {
                            // Don't do anything
                        }
                    }
                }

                if (!success)
                {
                    getLog().info("Waiting for " + url + (startingUp ? "" : " to stop"));
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                        interrupted = true;
                        break;
                    }
                }
            }

            if (!success)
            {
                throw new MojoExecutionException(String.format("The product %s didn't %s after %ds at %s. %s",
                        product.getInstanceId(), startingUp ? "start" : "stop", TimeUnit.MILLISECONDS.toSeconds(timeout), url, lastMessage));
            }
        }
    }

    /**
     * @return the list of instances for the product 'studio'
     */
    private Iterator<ProductExecution> getStudioExecutions(final List<ProductExecution> productExecutions)
    {
        return Iterables.filter(productExecutions, new Predicate<ProductExecution>(){

            @Override
            public boolean apply(ProductExecution input)
            {
                return input.getProductHandler() instanceof StudioProductHandler;
            }}).iterator();
    }


    /**
     * If there is any Studio instance, returns a list with all products requested by this instance.
     *
     * Configures both the Studio instance and its dependent products.
     *
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

            // Add everyone at the end of the list of products to execute. We don't check for duplicates, users shouldn't add studio products
            // to test groups, especially if they already have a Studio.
            productExecutionsIncludingStudio.addAll(dependantProducts);
        }

        return productExecutionsIncludingStudio;
    }

    protected abstract void doExecute() throws MojoExecutionException, MojoFailureException;

    protected void setParallelMode(List<ProductExecution> executions)
    {
        // Apply the configuration of the mojo to the products
        for (ProductExecution execution : executions)
        {
            Product product = execution.getProduct();
            if (parallel)
            {
                if (product.getSynchronousStartup() == null)
                {
                    product.setSynchronousStartup(Boolean.FALSE);
                }
            }
            else
            {
                product.setSynchronousStartup(Boolean.TRUE);
            }
        }
    }
}
