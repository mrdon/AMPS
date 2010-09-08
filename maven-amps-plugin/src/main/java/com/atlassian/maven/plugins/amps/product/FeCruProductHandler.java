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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Path;

import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.util.ant.AntJavaExecutorThread;
import com.atlassian.maven.plugins.amps.util.ant.JavaTaskFactory;

import static com.atlassian.maven.plugins.amps.util.ant.JavaTaskFactory.*;

public class FeCruProductHandler extends AbstractProductHandler
{
    private static final int STARTUP_CHECK_DELAY = 1000;
    private static final int STARTUP_CHECK_MAX = 1000 * 60 * 3; //todo is 3 mins enough?
    private final PluginProvider pluginProvider = new FeCruPluginProvider();
    private final Log log;
    private final JavaTaskFactory javaTaskFactory;
    
    public FeCruProductHandler(MavenProject project, MavenGoals goals, Log log)
    {
        super(project, goals);
        this.log = log;
        this.javaTaskFactory = new JavaTaskFactory(log);
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
        if (ctx.getJvmArgs() == null)
        {
            ctx.setJvmArgs("-Xmx512m -XX:MaxPermSize=160m");
        }

        extractAndProcessHomeDirectory(ctx);
        addArtifacts(ctx);

        // add application overrides
        try
        {
            addOverrides(ctx);
        }
        catch (IOException e)
        {
            throw new MojoExecutionException("Unable to override app files using src/test/resources/" + ctx.getInstanceId() + "-app", e);
        }

        log.info("Starting " + ctx.getInstanceId() + " on ports "
                + ctx.getHttpPort() + " (http) and " + controlPort(ctx.getHttpPort()) + " (control)");

        AntJavaExecutorThread thread;
        try
        {
            thread = execFishEyeCmd("run", ctx);
        }
        catch (Exception e)
        {
            throw new MojoExecutionException("Error starting fisheye.", e);
        }

        waitForFishEyeToStart(ctx, thread);
        
        return ctx.getHttpPort();
    }

    private void addOverrides(Product ctx) throws IOException
    {
        final File srcDir = new File(project.getBasedir(), "src/test/resources/" + ctx.getInstanceId() + "-app");
        if (srcDir.exists() && getHomeDirectory(ctx).exists())
        {
            FileUtils.copyDirectory(srcDir, getHomeDirectory(ctx));
        }
    }

    private void waitForFishEyeToStart(Product ctx, AntJavaExecutorThread thread) throws MojoExecutionException
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

            if (thread.isFinished())
            {
                throw new MojoExecutionException("Fisheye failed to start.", thread.getBuildException());
            }
            
            if (waited++ * STARTUP_CHECK_DELAY > STARTUP_CHECK_MAX)
            {
                throw new MojoExecutionException("FishEye took longer than " + STARTUP_CHECK_MAX + "ms to start!");
            }
        }
    }

    public void stop(Product ctx) throws MojoExecutionException
    {
        log.info("Stopping " + ctx.getInstanceId() + " on ports "
                + ctx.getHttpPort() + " (http) and " + controlPort(ctx.getHttpPort()) + " (control)");
        try
        {
            execFishEyeCmd("stop", ctx);
        }
        catch (Exception e)
        {
            throw new MojoExecutionException("Failed to stop FishEye/Crucible instance at " + ctx.getServer() + ":" + ctx.getHttpPort());
        }
        
        waitForFishEyeToStop(ctx);
    }

    private void waitForFishEyeToStop(Product ctx) throws MojoExecutionException
    {
        boolean connected = true;
        int waited = 0;
        while (connected)
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
            }
            catch (IOException e)
            {
                connected = false;
            }

            if (waited++ * STARTUP_CHECK_DELAY > STARTUP_CHECK_MAX)
            {
                throw new MojoExecutionException("FishEye took longer than " + STARTUP_CHECK_MAX + "ms to stop!");
            }
        }
    }

    private AntJavaExecutorThread execFishEyeCmd(String bootCommand, Product ctx) throws MojoExecutionException
    {
        final Map<String, String> properties = mergeSystemProperties(ctx);

        Java java = javaTaskFactory.newJavaTask(output(ctx.getOutput()).systemProperties(properties).jvmArgs(ctx.getJvmArgs()));

        Path classpath = java.createClasspath();
        classpath.createPathElement().setLocation(new File(getHomeDirectory(ctx), "fisheyeboot.jar"));
        
        java.setClassname("com.cenqua.fisheye.FishEyeCtl");
        
        java.createArg().setValue(bootCommand);
        
        AntJavaExecutorThread javaThread = new AntJavaExecutorThread(java);
        javaThread.start();

        return javaThread;
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
        final File homeDir = getHomeDirectory(ctx);
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
                            ctx.getDataVersion()));
    
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
            replace(configXml, "@SITE_URL@", String.valueOf(siteUrl(ctx)));
        } else {
            log.info("Using existing FishEye/Crucible application and instance data.");
        }
    }

    private String siteUrl(Product ctx)
    {
        return "http://" + ctx.getServer() + ":" + ctx.getHttpPort() + ctx.getContextPath();
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
            File homeDirectory = getHomeDirectory(ctx);
            final File pluginsDir = new File(homeDirectory, "var/plugins");
            createDirectory(pluginsDir);
            final File bundledPluginsDir = new File(pluginsDir, "bundled");
            createDirectory(bundledPluginsDir);
            final File userPluginsDir = new File(pluginsDir, "user");
            createDirectory(userPluginsDir);

            // add bundled plugins (todo these are already part of the dist.. this step is possibly unnecessary?)
            final File bundledPluginsZip = new File(homeDirectory, "plugins/bundled-plugins.zip");
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
            addArtifactsToDirectory(artifacts, new File(homeDirectory, "lib"));

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

    protected Map<String, String> getSystemProperties(Product ctx)
    {
        return Collections.emptyMap();
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
