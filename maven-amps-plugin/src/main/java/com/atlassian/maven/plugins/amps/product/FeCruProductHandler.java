package com.atlassian.maven.plugins.amps.product;

import static com.atlassian.core.util.FileUtils.createZipFile;
import static com.atlassian.maven.plugins.amps.util.ConfigFileUtils.replace;
import static com.atlassian.maven.plugins.amps.util.ZipUtils.unzip;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;

public class FeCruProductHandler extends AbstractProductHandler
{

    /**
     * JVM shutdown hook to terminate child FishEye processes when mojo dies *
     */
    private static Thread fisheyeShutdownHook;

    private static final int STARTUP_CHECK_DELAY = 1000;
    private static final int STARTUP_CHECK_MAX = 1000 * 60 * 3; //todo is 3 mins enough?
    private final PluginProvider pluginProvider = new FeCruPluginProvider();
    private final Log log;

    public FeCruProductHandler(MavenProject project, MavenGoals goals, Log log)
    {
        super(project, goals);
        this.log = log;
    }

    public String getId()
    {
        return ProductHandlerFactory.FECRU;
    }

    public int getDefaultHttpPort()
    {
        return 3990;
    }

    public int start(Product ctx) throws MojoExecutionException
    {
        extractAndProcessHomeDirectory(ctx);
        addArtifacts(ctx);

        // add application overrides
        try
        {
            addOverrides(ctx);
        }
        catch (IOException e)
        {
            throw new MojoExecutionException("Unable to override app files using src/test/resources/" + ctx.getId() + "-app", e);
        }

        try
        {
            execFishEyeCmd(ctx, "run", true);
        }
        catch (Exception e)
        {
            throw new MojoExecutionException("Error starting fisheye.", e);
        }

        waitForFishEyeToStart(ctx);

        return ctx.getHttpPort();
    }

    private void addOverrides(Product ctx) throws IOException
    {
        final File srcDir = new File(project.getBasedir(), "src/test/resources/" + ctx.getId() + "-app");
        if (srcDir.exists() && getHomeDirectory().exists())
        {
            FileUtils.copyDirectory(srcDir, getHomeDirectory());
        }
    }

    private void waitForFishEyeToStart(Product ctx) throws MojoExecutionException
    {
        boolean connected = false;
        int waited = 0;
        while (!connected)
        {
            try
            {
                Thread.sleep(STARTUP_CHECK_DELAY);
            }
            catch (InterruptedException e)
            {
                // ignore
            }
            try
            {
                new Socket("localhost", ctx.getHttpPort()).close();
                connected = true;
            }
            catch (IOException e)
            {
                // ignore
            }

            if (waited++ * STARTUP_CHECK_DELAY > STARTUP_CHECK_MAX)
            {
                throw new MojoExecutionException("FishEye took longer than " + STARTUP_CHECK_MAX + "ms to start!");
            }
        }
    }

    public void stop(Product ctx) throws MojoExecutionException
    {
        try
        {
            execFishEyeCmd(ctx, "stop", false);
            //orderly shutdown means we no longer need a shutdown hook
            clearShutdownHook();
        }
        catch (Exception e)
        {
            throw new MojoExecutionException("Failed to stop FishEye/Crucible instance at " + ctx.getServer() + ":" + ctx.getHttpPort());
        }
    }

    private void execFishEyeCmd(Product ctx, String bootCommand, boolean registerShutdownHook) throws MojoExecutionException
    {
        List<String> cmdParams = new ArrayList<String>();
        cmdParams.add("java");
        if (ctx.getJvmArgs() != null) {
            cmdParams.addAll(Arrays.asList(ctx.getJvmArgs().split("\\s")));
        } else {
            cmdParams.add("-Xmx512m");
            cmdParams.add("-XX:MaxPermSize=160m");
        }
        cmdParams.addAll(Arrays.asList("-jar", "fisheyeboot.jar"));
        cmdParams.add(bootCommand);

        ProcessBuilder builder = new ProcessBuilder(cmdParams);
        builder.directory(getHomeDirectory());
        try
        {
            Process process = builder.start();
            if (registerShutdownHook)
            {
                registerShutdownHook(process);
            }
        }
        catch (IOException e)
        {
            throw new MojoExecutionException("Failed to execute fisheye command '" + bootCommand + "'", e);
        }

        log.info("Started FishEye/Crucible.");
    }

    private File getBuildDirectory()
    {
        return new File(project.getBuild().getDirectory());
    }

    /**
     * Unpack the application and its sample home diectory from the two artifact zip files.
     * If the application already exists this isn't repeated, so you can keep your data until atlas-clean is run.
     *
     * @param ctx
     * @throws MojoExecutionException
     */
    private void extractAndProcessHomeDirectory(Product ctx) throws MojoExecutionException
    {
        final File homeDir = getHomeDirectory();
        final File varDirectory = new File(homeDir, "var");
        if (!varDirectory.exists()) {
            final File cruDistZip = goals.copyDist(getBuildDirectory(),
                    new ProductArtifact(
                            "com.atlassian.crucible",
                            "atlassian-crucible",
                            ctx.getVersion()));
    
            final File ampsDistZip = goals.copyHome(getBuildDirectory(),
                    new ProductArtifact(
                            "com.atlassian.fecru",
                            "amps-fecru",
                            ctx.getProductDataVersion()));
    
            createDirectory(homeDir);
            try
            {
                unzip(cruDistZip, homeDir.getPath(), 1);
                unzip(ampsDistZip, homeDir.getPath());
            }
            catch (final IOException ex)
            {
                throw new MojoExecutionException("Unable to extract ZIP artifacts into home directory", ex);
            }

            //setup config.xml, ports, test repos, whatever
            final File configXml = new File(homeDir, "config.xml");
            replace(configXml, "@CONTROL_BIND@", String.valueOf(controlPort(ctx.getHttpPort())));
            replace(configXml, "@HTTP_BIND@", String.valueOf(ctx.getHttpPort()));
            replace(configXml, "@HTTP_CONTEXT@", String.valueOf(ctx.getContextPath()));
            replace(configXml, "@HOME_DIR@", String.valueOf(homeDir.getAbsolutePath()));
        } else {
            log.info("Using existing FishEye/Crucible application and instance data.");
        }
    }

    private List<ProductArtifact> getPluginsArtifacts(final Product ctx)
    {
        final List<ProductArtifact> artifacts = new ArrayList<ProductArtifact>();
        //artifacts.addAll(getDefaultPlugins());
        artifacts.addAll(ctx.getPluginArtifacts());

        if (ctx.getSalVersion() != null)
        {
            artifacts.add(new ProductArtifact("com.atlassian.sal", "sal-api", ctx.getSalVersion()));
            artifacts.add(new ProductArtifact("com.atlassian.sal", "sal-fisheye-plugin", ctx.getSalVersion()));
        }

        if (ctx.getPdkVersion() != null)
        {
            artifacts.add(new ProductArtifact("com.atlassian.pdkinstall", "pdkinstall-plugin", ctx.getPdkVersion()));
        }

        if (ctx.getRestVersion() != null)
        {
            artifacts.add(new ProductArtifact("com.atlassian.plugins.rest", "atlassian-rest-module", ctx.getRestVersion()));
        }

        return artifacts;
    }

    private void createDirectory(File dir) throws MojoExecutionException
    {
        if (!dir.exists() && !dir.mkdirs()) {
                throw new MojoExecutionException("Failed to create directory " + dir.getAbsolutePath());
        }
    }

    //todo has alot in common with the AbstractWebappProductHandler
    private void addArtifacts(final Product ctx) throws MojoExecutionException
    {
        try
        {
            final File pluginsDir = new File(getHomeDirectory(), "var/plugins");
            createDirectory(pluginsDir);
            final File bundledPluginsDir = new File(pluginsDir, "bundled");
            createDirectory(bundledPluginsDir);
            final File userPluginsDir = new File(pluginsDir, "user");
            createDirectory(userPluginsDir);

            // add bundled plugins (todo these are already part of the dist.. this step is possibly unnecessary?)
            final File bundledPluginsZip = new File(getHomeDirectory(), "plugins/bundled-plugins.zip");
            if (bundledPluginsZip.exists())
            {
                unzip(bundledPluginsZip, bundledPluginsDir.getPath());
            }

            if (isStaticPlugin())
            {
                throw new MojoExecutionException("According to your atlassian-plugin.xml file, this plugin is not " +
                        "atlassian-plugins version 2. FishEye / Crucible currently only supports atlassian-plugins " +
                        "version 2.");
            }

            // add this plugin itself if enabled

            if (ctx.isInstallPlugin())
            {
                addThisPluginToDirectory(userPluginsDir);
            }

            // add plugins2 plugins
            addArtifactsToDirectory(pluginProvider.provide(ctx), userPluginsDir);
            addArtifactsToDirectory(getPluginsArtifacts(ctx), userPluginsDir);

            List<ProductArtifact> artifacts = new ArrayList<ProductArtifact>();
            //artifacts.addAll(getDefaultLibPlugins()); -- we don't support plugins 1
            artifacts.addAll(ctx.getLibArtifacts());
            addArtifactsToDirectory(artifacts, new File(getHomeDirectory(), "lib"));

            artifacts = new ArrayList<ProductArtifact>();
            //artifacts.addAll(getDefaultBundledPlugins()); -- todo default bundled plugins?
            artifacts.addAll(ctx.getBundledArtifacts());

            addArtifactsToDirectory(artifacts, bundledPluginsDir);

            if (bundledPluginsDir.list().length > 0)
            {
                createZipFile(bundledPluginsDir, bundledPluginsZip);
            }

            // todo add log4j.properties file if specified
//            if (ctx.getLog4jProperties() != null)
//            {
//                FileUtils.copyFile(ctx.getLog4jProperties(), new File(webappDir, "WEB-INF/classes/log4j.properties"));
//            }

        }
        catch (final Exception e)
        {
            throw new MojoExecutionException("Failed to add plugin artifacts", e);
        }
    }

    /**
     * The control port is the httpPort with a "1" appended to it //todo doc this
     */
    private int controlPort(int httpPort)
    {
        return httpPort * 10 + 1;
    }

    private static void registerShutdownHook(final Process p)
    {
        fisheyeShutdownHook = new Thread()
        {
            @Override
            public void run()
            {
                p.destroy();
            }
        };
        Runtime.getRuntime().addShutdownHook(fisheyeShutdownHook);
    }

    private static void clearShutdownHook()
    {
        if (fisheyeShutdownHook != null)
        {
            Runtime.getRuntime().removeShutdownHook(fisheyeShutdownHook);
            fisheyeShutdownHook = null;
        }
    }

    private static class FeCruPluginProvider extends AbstractPluginProvider
    {

        @Override
        protected Collection<ProductArtifact> getSalArtifacts(String salVersion)
        {
            return Arrays.asList(
                    new ProductArtifact("com.atlassian.sal", "sal-api", salVersion),
                    new ProductArtifact("com.atlassian.sal", "sal-fisheye-plugin", salVersion)
            );
        }
    }

}
