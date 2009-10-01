package com.atlassian.maven.plugins.amps.product;

import static com.atlassian.core.util.FileUtils.createZipFile;
import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import static com.atlassian.maven.plugins.amps.util.ConfigFileUtils.replace;
import static com.atlassian.maven.plugins.amps.util.ZipUtils.unzip;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;
import java.lang.reflect.Method;

public class FeCruProductHandler extends AbstractProductHandler
{

    private static final int STARTUP_CHECK_DELAY = 1000;
    private static final int STARTUP_CHECK_MAX = 1000 * 60 * 3; //todo is 3 mins enough?
    private final PluginProvider pluginProvider = new FeCruPluginProvider();

    public FeCruProductHandler(MavenProject project, MavenGoals goals)
    {
        super(project, goals);
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

        try
        {
            execFishEyeCmd("run");
        }
        catch (Exception e)
        {
            throw new MojoExecutionException("Error starting fisheye.", e);
        }

        waitForFishEyeToStart(ctx);

        return ctx.getHttpPort();
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
            execFishEyeCmd("stop");
        }
        catch (Exception e)
        {
            throw new MojoExecutionException("Failed to stop FishEye/Crucible instance at " + ctx.getServer() + ":" + ctx.getHttpPort());
        }
    }

    private void execFishEyeCmd(String bootCommand) throws MojoExecutionException
    {

        try {
            ClassLoader cl = new URLClassLoader(new URL[] {new URL("file:" + new File(getHomeDirectory(), "fisheyeboot.jar").getAbsolutePath())});
            Class<?> fisheyeCtl = cl.loadClass("com.cenqua.fisheye.FishEyeCtl");
            Method main = fisheyeCtl.getDeclaredMethod("mainImpl", String[].class);
            main.invoke(null, new Object[] {new String[] {bootCommand}});
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to execute fisheye command '" + bootCommand + "'", e);
        }


        // todo logging
        // java.setOutput(getContainerOutputLog());

        //getLog().info("Started Fisheye.");
    }

    private File getBuildDirectory()
    {
        return new File(project.getBuild().getDirectory());
    }

    private void extractAndProcessHomeDirectory(Product ctx) throws MojoExecutionException
    {
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

        final File homeDir = getHomeDirectory();
        homeDir.mkdir();
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

    //todo has alot in common with the AbstractWebappProductHandler
    private void addArtifacts(final Product ctx) throws MojoExecutionException
    {
        try
        {
            final File pluginsDir = new File(getHomeDirectory(), "var/plugins");
            pluginsDir.mkdir();
            final File bundledPluginsDir = new File(pluginsDir, "bundled");
            bundledPluginsDir.mkdir();
            final File userPluginsDir = new File(pluginsDir, "user");
            userPluginsDir.mkdir();

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
