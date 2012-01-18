package com.atlassian.maven.plugins.amps.product.studio;

import static com.atlassian.maven.plugins.amps.product.ProductHandlerFactory.STUDIO;
import static com.atlassian.maven.plugins.amps.product.ProductHandlerFactory.STUDIO_BAMBOO;
import static com.atlassian.maven.plugins.amps.product.ProductHandlerFactory.STUDIO_CONFLUENCE;
import static com.atlassian.maven.plugins.amps.product.ProductHandlerFactory.STUDIO_CROWD;
import static com.atlassian.maven.plugins.amps.product.ProductHandlerFactory.STUDIO_FECRU;
import static com.atlassian.maven.plugins.amps.product.ProductHandlerFactory.STUDIO_JIRA;
import static com.atlassian.maven.plugins.amps.util.ZipUtils.unzip;
import static org.apache.commons.io.FileUtils.copyDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.surefire.shade.org.apache.commons.lang.StringUtils;

import com.atlassian.maven.plugins.amps.MavenContext;
import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.product.AmpsProductHandler;
import com.atlassian.maven.plugins.amps.product.ProductHandler;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import com.atlassian.maven.plugins.amps.util.ConfigFileUtils;
import com.atlassian.maven.plugins.amps.util.ConfigFileUtils.Replacement;
import com.atlassian.maven.plugins.amps.util.ProjectUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * This product handler is a 'ghost'. It doesn't start a real product, but it prepares the environment
 * for all studio-based products.
 * @since 3.6
 */
final public class StudioProductHandler extends AmpsProductHandler
{
    private static final String STUDIO_PROPERTIES = "home/studio.properties";
    private static final String STUDIO_TEST_PROPERTIES = "studiotest.properties";
    private static final String STUDIO_INITIAL_DATA_PROPERTIES = "home/studio-initial-data.properties";
    private static final String DEVMODE_HAL_LICENSES_XML = "home/devmode-hal-licenses.xml";
    private static final String STUDIO_INITIAL_DATA_XML = "home/studio-initial-data.xml";

    /** This token is used in product's <version> when they want to reuse the Studio product's version */
    private static final String STUDIO_VERSION_TOKEN = "STUDIO-VERSION";

    private static final Map<String, String> defaultContextPaths = new HashMap<String, String>()
    {
        {
            put(STUDIO_BAMBOO, "/builds");
            put(STUDIO_CONFLUENCE, "/wiki");
            put(STUDIO_CROWD, "/crowd");
            put(STUDIO_FECRU, "/");
            put(STUDIO_JIRA, "/jira");
        }
    };

    private static final Map<String, Integer> defaultDebugPorts = new HashMap<String, Integer>()
    {
        {
            put(STUDIO_BAMBOO, 5011);
            put(STUDIO_CONFLUENCE, 5007);
            put(STUDIO_CROWD, 5003);
            put(STUDIO_FECRU, 5005);
            put(STUDIO_JIRA, 5009);
        }
    };

    public StudioProductHandler(MavenContext context, MavenGoals goals)
    {
        super(context, goals);
    }



    @Override
    protected ProductArtifact getTestResourcesArtifact()
    {
        return new ProductArtifact("com.atlassian.studio", "studio-test-resources");
    }



    @Override
    public String getId()
    {
        return STUDIO;
    }

    /**
     * Returns the list of products that are configured in this studio instance, as defined in 'instanceIds'
     *
     * @param studioContext
     *            the Studio product
     * @return a list of instance ids. Never null.
     * @throws MojoExecutionException
     *             if the Studio product is misconfigured
     */
    public List<String> getDependantInstances(Product studioContext) throws MojoExecutionException
    {
        StudioProperties studioProperties = getStudioProperties(studioContext);
        studioProperties.setStudioProduct(studioContext);
        List<String> instanceIds = studioContext.getInstanceIds();
        if (instanceIds.isEmpty())
        {
            instanceIds.add(STUDIO_CROWD);
            instanceIds.add(STUDIO_JIRA);
            instanceIds.add(STUDIO_CONFLUENCE);
            instanceIds.add(STUDIO_BAMBOO);
            instanceIds.add(STUDIO_FECRU);
        }
        return instanceIds;
    }

    /**
     * Prepares the studio home. Does not start any application.
     *
     */
    @Override
    public int start(Product ctx) throws MojoExecutionException
    {
        // Sanity check
        sanityCheck(ctx);

        // Launch the product
        createStudioHome(ctx);

        // The symlink is pretty much constrained
        // - must be in /target (the work dir for Bamboo)
        // - must be 2 levels up from the studio home
        File symlink = new File(context.getProject().getBuild().getDirectory(), "svn");
        if (!symlink.exists())
        {
            // Create a symlink so that Bamboo can work
            createSymlink(ctx.getStudioProperties().getSvnRoot(), symlink);
        }

        return 0;
    }

    /**
     * Checks the configuration to throw exceptions early for the few most common problems
     */
    private void sanityCheck(Product studioProduct) throws MojoExecutionException
    {
        StudioProperties properties = studioProduct.getStudioProperties();
        if (properties == null)
        {
            throw new MojoExecutionException(String.format("Something went wrong when starting %s. The 'studio' handler was not initialised propertly.",
                    studioProduct.getInstanceId()));
        }
        if (properties.getCrowd() == null || properties.getCrowd().getStudioProperties() == null)
        {
            log.error(String.format(
                    "You won't be able to run %s, Studio-Crowd was not configured properly.", studioProduct.getInstanceId()));
        }
        if (properties.isJiraEnabled() && (properties.getJira() == null || properties.getJira().getStudioProperties() == null))
        {
            log.error(String.format(
                    "You won't be able to run %s, Studio-JIRA was not configured properly.", studioProduct.getInstanceId()));
        }
        if (properties.isConfluenceEnabled() && (properties.getConfluence() == null || properties.getConfluence().getStudioProperties() == null))
        {
            log.error(String.format(
                    "You won't be able to run %s, Studio-Confluence was not configured properly.", studioProduct.getInstanceId()));
        }
        if (properties.isFisheyeEnabled() && (properties.getFisheye() == null || properties.getFisheye().getStudioProperties() == null))
        {
            log.error(String.format(
                    "You won't be able to run %s, Studio-Fisheye was not configured properly.", studioProduct.getInstanceId()));
        }
        if (properties.isBambooEnabled() && (properties.getBamboo() == null || properties.getBamboo().getStudioProperties() == null))
        {
            log.error(String.format(
                    "You won't be able to run %s, Studio-Bamboo was not configured properly.", studioProduct.getInstanceId()));
        }
    }

    @Override
    public void stop(Product ctx) throws MojoExecutionException
    {
        // Delete the symlink so that the mvn clean:clean works properly
        File symlink = new File(context.getProject().getBuild().getDirectory(), "svn");
        symlink.deleteOnExit();

        // Nothing to stop
    }

    @Override
    public int getDefaultHttpPort()
    {
        // No default - this product can't be launched
        return 0;
    }

    /**
     * Does nothing for non-studios products.
     * For Studio products, defaults the studio-specific properties.
     *
     * @param product
     *            a product. All products are accepted but not all of the will be
     *            modified. The product must have an instanceId.
     */
    public static void setDefaultValues(MavenContext context, Product product)
    {
        // Set the default context path
        // Amps requires '/' before and not after
        String defaultContextPath = defaultContextPaths.get(product.getId());
        if (defaultContextPath != null)
        {
            // It's a Studio product
            if (product.getOutput() == null)
            {
                product.setOutput(new File(context.getProject().getBuild().getDirectory(), product.getInstanceId() + ".log").getAbsolutePath());
            }
            if (product.getContextPath() == null)
            {
                product.setContextPath(defaultContextPath);
            }
            if (product.getVersion() == null)
            {
                // This value will be replaced with the version given by the studio product.
                // We can't leave it empty because the value will be defaulted to RELEASE.
                product.setVersion(STUDIO_VERSION_TOKEN);
            }

            // Set the default debug port
            if (product.getJvmDebugPort() == 0)
            {
                product.setJvmDebugPort(defaultDebugPorts.get(product.getId()));
            }

            // StudioFecru only
            if (product.getShutdownEnabled() == null)
            {
                product.setShutdownEnabled(Boolean.TRUE);
            }
        }
    }

    /**
     * Requests the Studio instance to configure its fellow products (home directory, ...)
     *
     * Not thread safe.
     *
     * @param studioContext
     *            the studio instance. There may be several Studio instances, so the products should be configured
     *            with this instance in mind.
     *
     * @param dependantProducts
     *            the list of products running 'in' this instance of studio (same home & applinked).
     *            The client should guarantee it calls this method once and only once for all the product on one
     *            Studio instance.
     * @throws MojoExecutionException
     */
    public void configure(Product studioContext, List<Product> dependantProducts) throws MojoExecutionException
    {
        StudioProperties studioProperties = getStudioProperties(studioContext);

        boolean confluenceStandalone = true;

        // Sets properties for each product
        for (Product product : dependantProducts)
        {
            // Each product provides some configuration info

            // JIRA, Confluence and Bamboo support the parallel startup;
            // Crowd must be started synchronously because there's a race condition
            // and Fisheye doesn't support parallel startup.

            if (STUDIO_CROWD.equals(product.getId()))
            {
                studioProperties.setCrowd(product);
                if (product.getSynchronousStartup() == null)
                {
                    product.setSynchronousStartup(Boolean.TRUE);
                }
            }
            else if (STUDIO_CONFLUENCE.equals(product.getId()))
            {
                studioProperties.setConfluence(product);
                if (product.getSynchronousStartup() == null)
                {
                    product.setSynchronousStartup(studioContext.getSynchronousStartup());
                }
            }
            else if (STUDIO_JIRA.equals(product.getId()))
            {
                studioProperties.setJira(product);
                confluenceStandalone = false;
                if (product.getSynchronousStartup() == null)
                {
                    product.setSynchronousStartup(studioContext.getSynchronousStartup());
                }
            }
            else if (STUDIO_FECRU.equals(product.getId()))
            {
                studioProperties.setFisheye(product);
                confluenceStandalone = false;
            }
            else if (STUDIO_BAMBOO.equals(product.getId()))
            {
                studioProperties.setBamboo(product);
                confluenceStandalone = false;
                if (product.getSynchronousStartup() == null)
                {
                    product.setSynchronousStartup(studioContext.getSynchronousStartup());
                }
            }
            else
            {
                throw new MojoExecutionException("A non-studio product was listed in a Studio instance: " + product.getInstanceId());
            }

            studioProperties.setModeConfluenceStandalone(confluenceStandalone);

            // And share the bean between all products
            product.setStudioProperties(studioProperties);

            // Set the common Studio version
            if (STUDIO_VERSION_TOKEN.equals(product.getVersion()))
            {
                product.setVersion(studioProperties.getVersion());
            }
        }

        // Sets the paths for non-products
        File studioHomeDir = getHomeDirectory(studioContext);
        File studioCommonsDir = studioHomeDir.getParentFile();
        File svnHomeDir = new File(studioCommonsDir, "svn-home");
        File webDavDir = new File(studioCommonsDir, "webdav-home");
        String svnPublicUrl;
        if (System.getProperty("os.name").toLowerCase(Locale.ENGLISH).contains("windows"))
        {
            svnPublicUrl = "file:///" + svnHomeDir.getAbsolutePath();
            log.warn("Studio is only designed to run on Linux systems.");
        }
        else
        {
            svnPublicUrl = "file://" + svnHomeDir.getAbsolutePath();
        }

        studioProperties.setStudioHome(studioHomeDir.getAbsolutePath());
        studioProperties.setSvnRoot(svnHomeDir.getAbsolutePath());
        studioProperties.setSvnPublicUrl(svnPublicUrl);
        studioProperties.setWebDavHome(webDavDir.getAbsolutePath());
    }

    /**
     * Return the studio properties. If it doesn't exist, create the bean.
     * Not thread safe.
     *
     * @param studioContext
     *            the Studio product
     * @return the properties, never null.
     */
    private static StudioProperties getStudioProperties(Product studioContext)
    {
        StudioProperties properties = studioContext.getStudioProperties();
        if (properties == null)
        {
            properties = new StudioProperties(studioContext);
            studioContext.setStudioProperties(properties);
        }
        return properties;
    }

    /**
     * Studio returns the parent of studio-home, to ship other application's homes:
     * <ul>
     * <li>studioInstance <b>(&lt;- the snapshot)</b></li>
     * <li>studioInstance/confluence-home</li>
     * <li>studioInstance/jira-home</li>
     * <li>studioInstance/...</li>
     * <li>studioInstance/home <b>(&lt;- the home)</b></li>
     * <ul>
     */
    @Override
    public File getSnapshotDirectory(Product studio)
    {
        return getHomeDirectory(studio).getParentFile();
    }


    /**
     * Fills the properties with the studio configuration.
     *
     * If the studio1-home directory does not exist, creates it and fills it with the right contents.
     * If this studio1-home exists, do not change the contents
     *
     * This method must be guaranteed to be called:
     * <ul>
     * <li>Exactly once for this StudioProperties bean.</li>
     * <li>After {@link #configure(Product, List)}.</li>
     * <li>Before any product's home is created or any product is started</li>
     * </ul>
     *
     * <p>
     * It also adds the svn home and the webdav home. The final tree is:
     * <ul>
     * <li>studioInstance1
     * <ul>
     * <li>home</li>
     * <li>studio-confluence</li>
     * <li>...</li>
     * <li>svn-home</li>
     * <li>webdav-home</li>
     * </ul>
     * </li>
     * </ul>
     * </p>
     *
     * @param studio
     *            the Studio properties. Must not be null.
     * @param buildirectory
     *            the base directory (you can obtain it using ((MavenProject)project).getBuild().getDirectory())
     * @throws MojoExecutionException
     *
     */
    // This method reproduces PrepareStudioMojo.groovy
    public void createStudioHome(Product studio) throws MojoExecutionException
    {
        StudioProperties properties = getStudioProperties(studio);

        // All homes are exported, including the studioInstanceId/home
        File studioHomeDir = new File(properties.getStudioHome());
        File studioCommonsDir = studioHomeDir.getParentFile();

        // Extracts the zip / copies the homes to studioInstanceId/
        if (!studioHomeDir.exists())
        {
            extractHome(studioCommonsDir, studio);
            if (!studioHomeDir.exists())
            {
                throw new MojoExecutionException("The Studio home zip must contain a '*/*/home' folder");
            }
        }

        File svnHomeDir = new File(properties.getSvnRoot());
        if (!svnHomeDir.exists())
        {
            throw new MojoExecutionException("The Studio home zip must contain a '*/*/svn-home' folder");
        }

        File webDavDir = new File(properties.getWebDavHome());
        if (!webDavDir.exists())
        {
            throw new MojoExecutionException("The Studio home zip must contain a '*/*/webdav-home' folder");
        }

        // Parametrise the files
        parameteriseFiles(studioCommonsDir, studio);

        // Set the system properties
        properties.overrideSystemProperty("studio.home", studioHomeDir.getAbsolutePath());
        properties.overrideSystemProperty("studio.initial.data.xml", new File(studioCommonsDir, STUDIO_INITIAL_DATA_XML).getAbsolutePath());
        properties.overrideSystemProperty("studio.initial.data.properties", new File(studioCommonsDir, STUDIO_INITIAL_DATA_PROPERTIES).getAbsolutePath());
        properties.overrideSystemProperty("studio.hal.instance.uri", new File(studioCommonsDir, DEVMODE_HAL_LICENSES_XML).getAbsolutePath());

        // Sets the home data for the products - we don't override productDataVersion because we
        // won't ship separate studio versions of homes.
        Product crowd = properties.getCrowd();
        if (crowd.getDataPath() == null)
        {
            crowd.setDataPath(new File(studioCommonsDir, "crowd-home").getAbsolutePath());
        }

        Product confluence = properties.getConfluence();
        if (confluence != null && confluence.getDataPath() == null)
        {
            confluence.setDataPath(new File(studioCommonsDir, "confluence-home").getAbsolutePath());
        }

        Product jira = properties.getJira();
        if (jira != null && jira.getDataPath() == null)
        {
            jira.setDataPath(new File(studioCommonsDir, "jira-home").getAbsolutePath());
        }

        Product bamboo = properties.getBamboo();
        if (bamboo != null && bamboo.getDataPath() == null)
        {
            bamboo.setDataPath(new File(studioCommonsDir, "bamboo-home").getAbsolutePath());
        }

        Product fecru = properties.getFisheye();
        if (fecru != null && fecru.getDataPath() == null)
        {
            fecru.setDataPath(new File(studioCommonsDir, "fecru-home").getAbsolutePath());
        }

        // Always override files regardless of home directory existing or not
        overrideAndPatchHomeDir(studioCommonsDir, studio);
    }

    private void parameteriseFiles(File studioSnapshotCopyDir, Product studio) throws MojoExecutionException
    {
        ConfigFileUtils.replace(getConfigFiles(studio, studioSnapshotCopyDir), getReplacements(studio), false, log);
    }

    @Override
    public List<File> getConfigFiles(Product studio, File studioSnapshotDir)
    {
        List<File> list = Lists.newArrayList();
        list.add(new File(studioSnapshotDir, STUDIO_PROPERTIES));
        list.add(new File(studioSnapshotDir, STUDIO_INITIAL_DATA_PROPERTIES));
        list.add(new File(studioSnapshotDir, STUDIO_INITIAL_DATA_XML));
        list.add(new File(studioSnapshotDir, DEVMODE_HAL_LICENSES_XML));
        list.add(new File(project.getBuild().getTestOutputDirectory(), STUDIO_TEST_PROPERTIES));

        list.add(new File(studioSnapshotDir, "fecru-home/config.xml"));
        list.add(new File(studioSnapshotDir, "confluence-home/database/confluencedb.log"));
        list.add(new File(studioSnapshotDir, "confluence-home/database/confluencedb.script"));
        list.add(new File(studioSnapshotDir, "jira-home/database.log"));
        list.add(new File(studioSnapshotDir, "jira-home/database.script"));
        list.add(new File(studioSnapshotDir, "bamboo-home/database.log"));
        list.add(new File(studioSnapshotDir, "bamboo-home/database.script"));
        return list;
    }

    /**
     * Both used to unzip and rezip the home
     */
    @Override
    public List<Replacement> getReplacements(final Product studio)
    {
        List<Replacement> replacements = super.getReplacements(studio);
        replacements.addAll(new ArrayList<Replacement>()
        {
            private void putIfNotNull(String key, String value)
            {
                putIfNotNull(key, value, true);
            }

            private void putIfNotNull(String key, String value, boolean reversible)
            {
                if (reversible && StringUtils.isNotBlank(value))
                {
                    add(new Replacement(key, value));
                }
                else if (value != null)
                {
                    add(new Replacement(key, value, false));
                }
            }

            // Static bloc for the anonymous subclass of ArrayList
            {
                StudioProperties properties = studio.getStudioProperties();
                putIfNotNull("%GREENHOPPER-LICENSE%", "test-classes/greenhopper.license");

                if (properties.isJiraEnabled())
                {
                    File attachmentsFolder = new File(getHomeDirectory(properties.getJira()), "attachments");
                    putIfNotNull("%JIRA-ATTACHMENTS%", attachmentsFolder.getAbsolutePath());
                    putIfNotNull("%JIRA-BASE-URL%", properties.getJiraUrl());
                    putIfNotNull("%JIRA-HOST-URL%", properties.getJiraHostUrl());
                    putIfNotNull("%JIRA-CONTEXT%", properties.getJiraContextPath(), false);
                }

                if (properties.isConfluenceEnabled())
                {
                    putIfNotNull("%CONFLUENCE-BASE-URL%", properties.getConfluenceUrl());
                    putIfNotNull("%CONFLUENCE-HOST-URL%", properties.getConfluenceHostUrl());
                    putIfNotNull("%CONFLUENCE-CONTEXT%", properties.getConfluenceContextPath(), false);
                }

                if (properties.isFisheyeEnabled())
                {
                    putIfNotNull("%FISHEYE-BASE-URL%", properties.getFisheyeUrl());
                    putIfNotNull("%FISHEYE-HOST-URL%", properties.getFisheyeHostUrl());
                    putIfNotNull("%FISHEYE-CONTROL-PORT%", properties.getFisheyeControlPort());
                    putIfNotNull("%FISHEYE-CONTEXT%", properties.getFisheyeContextPath(), false);
                    putIfNotNull("%FISHEYE-SHUTDOWN-ENABLED%", String.valueOf(firstNotNull(properties.getFisheyeShutdownEnabled(), Boolean.TRUE)));
                }

                if (properties.isBambooEnabled())
                {
                    putIfNotNull("%BAMBOO-BASE-URL%", properties.getBambooUrl());
                    putIfNotNull("%BAMBOO-HOST-URL%", properties.getBambooHostUrl());
                    putIfNotNull("%BAMBOO-CONTEXT%", properties.getBambooContextPath(), false);
                    putIfNotNull("%BAMBOO-ENABLED%", "true", false);
                }
                else
                {
                    putIfNotNull("%BAMBOO-ENABLED%", "false", false);
                }

                putIfNotNull("%CROWD-BASE-URL%", properties.getCrowdUrl());
                putIfNotNull("%CROWD-HOST-URL%", properties.getCrowdHostUrl());
                putIfNotNull("%CROWD-CONTEXT%", properties.getCrowdContextPath(), false);

                putIfNotNull("%SVN-BASE-URL%", properties.getSvnRoot());
                putIfNotNull("%SVN-PUBLIC-URL%", properties.getSvnPublicUrl());
                putIfNotNull("%SVN-HOOKS%", properties.getSvnHooks());

                putIfNotNull("%STUDIO-DATA-LOCATION%", "", false);
                putIfNotNull("%STUDIO-HOME%", properties.getStudioHome());
                putIfNotNull("%GAPPS-ENABLED%", Boolean.toString(properties.isGappsEnabled()), false);
                if (properties.isGappsEnabled())
                {
                    putIfNotNull("%GAPPS-ENABLED%", Boolean.toString(true), false);
                    putIfNotNull("%STUDIO-GAPPS-DOMAIN%", properties.getGappsDomain());
                }
                else
                {
                    putIfNotNull("%GAPPS-ENABLED%", Boolean.toString(false), false);
                }
                putIfNotNull("%STUDIO-WEBDAV-DIRECTORY%", properties.getWebDavHome());
                putIfNotNull("%STUDIO-SVN-IMPORT-STAGING-DIRECTORY%", properties.getSvnImportStagingDirectory());

                try
                {
                    putIfNotNull("%SVN_HOME_URL_ENCODED%", URLEncoder.encode(propertiesEncode(properties.getSvnRoot()), "UTF-8"));
                }
                catch (UnsupportedEncodingException badJvm)
                {
                    throw new RuntimeException("UTF-8 should be supported on any JVM", badJvm);
                }

            }
        });
        return replacements;
    }

    private void createSymlink(String source, File target) throws MojoExecutionException
    {

        if (System.getProperty("os.name").toLowerCase(Locale.ENGLISH).contains("windows"))
        {
            log.error("Studio is designed to run on Linux systems. As you can't create a symbolic link for SVN, you " +
                    "will have problems using SVN, FishEye and Bamboo, and possibly the other products.");
            return;
        }


        String[] systemCommand = {
                "ln",
                "-s",
                source,
                target.getAbsolutePath()
        };
        try
        {

            Process symlinkCreation = Runtime.getRuntime().exec(systemCommand);

            // In case of errors, write the message and wait for the user to acknowledge
            BufferedReader errorStream = new BufferedReader(
                    new InputStreamReader(symlinkCreation.getErrorStream()));
            String errorLine = null;
            boolean hasErrors = false;
            while ((errorLine = errorStream.readLine()) != null)
            {
                if (!hasErrors)
                {
                    System.err.println("Error while executing " + systemCommand + ": ");
                    hasErrors = true;
                }
                System.err.println(errorLine);
            }
            if (hasErrors)
            {
                System.out.println("Please execute this command in your command line and press a key to continue");
                System.in.read();
            }
        }
        catch (IOException e)
        {
            throw new MojoExecutionException("Could not create the symlink: " + systemCommand, e);
        }
    }

    /**
     * Copies/Extracts the data into parent/directoryName
     * @throws MojoExecutionException
     */
    private void extractHome(File target, Product studio) throws MojoExecutionException
    {
        // Take whichever is provided by the user (dataPath or productDataVersion zip to download)
        File testResourcesZip = getProductHomeData(studio);

        try
        {
            if (!testResourcesZip.exists())
            {
                throw new MojoExecutionException(String.format("This source doesn't exist: %s", testResourcesZip.getAbsoluteFile()));
            }
            if (testResourcesZip.isDirectory())
            {
                copyDirectory(testResourcesZip, target);
            }
            else
            {
                unzip(testResourcesZip, target.getAbsolutePath(), 2);
            }
        }
        catch (IOException ioe)
        {
            throw new MojoExecutionException(String.format("Unable to copy/unzip the studio home from %s to %s", testResourcesZip.getAbsolutePath(),
                    target.getAbsolutePath()), ioe);
        }
    }

    /**
     * Performs the necessary initialisation for Studio products' homes
     */
    static void processProductsHomeDirectory(Log log, Product ctx, File homeDir) throws MojoExecutionException
    {
        // Nothing to process in the home.
        // Just check Studio has been configured.
        if (ctx.getStudioProperties() == null)
        {
            throw new MojoExecutionException(String.format("%s product is dependant on Studio. You must include the Studio product in your execution.",
                    ctx.getInstanceId()));
        }
    }

    /**
     * Performs the necessary initialisation for Studio products
     *
     * @param log
     * @param ctx
     * @param homeDir
     * @param explodedWarDir
     * @param crowdPropertiesPath
     *            the path from the explodedWarDir to the crowd.properties
     * @throws MojoExecutionException
     */
    static void addProductHandlerOverrides(Log log, Product ctx, File homeDir, File explodedWarDir) throws MojoExecutionException
    {
        addProductHandlerOverrides(log, ctx, homeDir, explodedWarDir, "WEB-INF/classes/crowd.properties");
    }

    /**
     * Performs the necessary initialisation for Studio products
     *
     * @param log
     * @param ctx
     * @param homeDir
     * @param explodedWarDir
     * @param crowdPropertiesPath
     *            the path from the explodedWarDir to the crowd.properties
     * @throws MojoExecutionException
     */
    static void addProductHandlerOverrides(Log log, Product ctx, File homeDir, File explodedWarDir, String crowdPropertiesPath) throws MojoExecutionException
    {
        File crowdProperties = new File(explodedWarDir, crowdPropertiesPath);
        if (checkFileExists(crowdProperties, log))
        {
            parametriseCrowdFile(crowdProperties, ctx.getStudioProperties().getCrowdUrl(), log);
        }
    }

    /**
     * Replaces the crowd url in the the crowd.properties of the current application
     *
     * @param crowdProperties
     *            the file "crowd.properties"
     * @param crowdUrl
     *            the Crowd url, example: "http://localhost:4990/crowd"
     * @throws MojoExecutionException
     *             if an error is encountered during the replacement
     */
    private static void parametriseCrowdFile(File crowdProperties, String crowdUrl, Log log) throws MojoExecutionException
    {
        List<Replacement> replacements = Lists.newArrayList();
        replacements.add(new Replacement("%CROWD-INTERNAL-URL%", crowdUrl));
        replacements.add(new Replacement("%CROWD-URL%", crowdUrl));

        ConfigFileUtils.replace(crowdProperties, replacements, false, log);
    }


    public void cleanupProductHomeForZip(Product studioProduct, File studioHome) throws MojoExecutionException
    {
        try
        {
            // Get products of this Studio instance
            StudioProperties studioProperties = studioProduct.getStudioProperties();

            // The key of this map is the name of the home folder for this application
            // Unused applications are "null", so they will not be seen in the map
            Map<String, Product> products = Maps.newHashMap();
            products.put("crowd-home", studioProperties.getCrowd());
            products.put("confluence-home", studioProperties.getConfluence());
            products.put("jira-home", studioProperties.getJira());
            products.put("fecru-home", studioProperties.getFisheye());
            products.put("bamboo-home", studioProperties.getBamboo());

            // Make each product's home
            for (String productHomeName : products.keySet())
            {
                Product product = products.get(productHomeName);
                if (product != null)
                {
                    File productDestinationDirectory = new File(studioHome, productHomeName);
                    File productHomeDirectory = getHomeDirectory(product);

                    // Delete studio1/{product}-home and replace it with the current product's home
                    if (productDestinationDirectory.exists())
                    {
                        FileUtils.deleteDirectory(productDestinationDirectory);
                    }
                    ProjectUtils.createDirectory(productDestinationDirectory);
                    copyDirectory(productHomeDirectory, productDestinationDirectory);

                }
            }

            // Un-parametrise the files
            super.cleanupProductHomeForZip(studioProduct, studioHome);

            // Request the products to clean their own files
            // Do it after Studio cleanup, because Studio will handle "svn-home" in Fecru, which Fecru can't do
            // (Fecru is not aware of Studio).
            for (String productHomeName : products.keySet())
            {
                Product product = products.get(productHomeName);
                if (product != null)
                {
                    File productDestinationDirectory = new File(studioHome, productHomeName);
                    ProductHandler handler = ProductHandlerFactory.create(product.getId(), context, goals);
                    handler.cleanupProductHomeForZip(product, productDestinationDirectory);
                }
            }
        }
        catch (IOException ioe)
        {
            throw new MojoExecutionException("Could not copy a product home directory.", ioe);
        }
    }

    static String fixWindowsSlashes(final String path)
    {
        return path.replaceAll("\\\\", "/");
    }

    static boolean checkFileExists(File file, Log log)
    {
        if (!file.exists())
        {
            log.warn(String.format("%s does not exist. Will skip customisation", file.getAbsolutePath()));
            return false;
        }
        return true;
    }

    /**
     * Returns the first value which is not null. Useful to set default values
     *
     * @param t
     * @return the first non-null value, or null if all values are null
     */
    public static <T> T firstNotNull(T... values)
    {
        for (T t : values)
        {
            if (t != null)
            {
                return t;
            }
        }
        return null;
    }
}
