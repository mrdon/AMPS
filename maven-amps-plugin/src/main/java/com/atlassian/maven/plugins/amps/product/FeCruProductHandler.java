package com.atlassian.maven.plugins.amps.product;

import static com.atlassian.maven.plugins.amps.util.ZipUtils.unzip;
import static com.atlassian.maven.plugins.amps.util.ant.JavaTaskFactory.output;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atlassian.maven.plugins.amps.MavenContext;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Path;

import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.util.ConfigFileUtils.Replacement;
import com.atlassian.maven.plugins.amps.util.ZipUtils;
import com.atlassian.maven.plugins.amps.util.ant.AntJavaExecutorThread;
import com.atlassian.maven.plugins.amps.util.ant.JavaTaskFactory;

import static com.atlassian.maven.plugins.amps.util.ProjectUtils.createDirectory;
import static com.atlassian.maven.plugins.amps.util.ProjectUtils.firstNotNull;

public class FeCruProductHandler extends AbstractProductHandler
{
    private static final int STARTUP_CHECK_DELAY = 1000;
    private static final String FISHEYE_INST = "fisheye.inst";

    private final JavaTaskFactory javaTaskFactory;

    public FeCruProductHandler(MavenContext context, MavenGoals goals)
    {
        super(context, goals, new FeCruPluginProvider());
        this.javaTaskFactory = new JavaTaskFactory(log);
    }

    public String getId()
    {
        return ProductHandlerFactory.FECRU;
    }

    protected ProductArtifact getArtifact()
    {
        return new ProductArtifact("com.atlassian.crucible", "atlassian-crucible", "RELEASE");
    }

    public int getDefaultHttpPort()
    {
        return 3990;
    }

    public final void stop(Product ctx) throws MojoExecutionException
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

    @Override
    protected void extractProductHomeData(File productHomeZip, File homeDir, Product ctx)
            throws MojoExecutionException
    {
        try
        {
            unzip(productHomeZip, homeDir.getPath());
        }
        catch (final IOException ex)
        {
            throw new MojoExecutionException("Unable to copy home directory", ex);
        }
    }

    @Override
    public List<Replacement> getReplacements(Product ctx)
    {
        List<Replacement> replacements = super.getReplacements(ctx);
        File homeDirectory = getHomeDirectory(ctx);
        replacements.add(new Replacement("@CONTROL_BIND@", String.valueOf(controlPort(ctx.getHttpPort()))));
        replacements.add(new Replacement("@HTTP_BIND@", String.valueOf(ctx.getHttpPort())));
        replacements.add(new Replacement("@HTTP_CONTEXT@", String.valueOf(ctx.getContextPath()), false));
        replacements.add(new Replacement("@HOME_DIR@", String.valueOf(homeDirectory.getAbsolutePath())));
        replacements.add(new Replacement("@SITE_URL@", String.valueOf(siteUrl(ctx))));

        // Note: Unfortunately, in config.xml, FeCru sometimes uses url encoding.
        return replacements;
    }

    @Override
    public List<File> getConfigFiles(Product product, File homeDir)
    {
        List<File> configFiles = super.getConfigFiles(product, homeDir);
        configFiles.add(new File(homeDir, "config.xml"));
        return configFiles;
    }

    @Override
    protected File extractApplication(Product ctx, File homeDir) throws MojoExecutionException
    {
        File appDir = createDirectory(getAppDirectory(ctx));

        ProductArtifact defaults = getArtifact();
        ProductArtifact artifact = new ProductArtifact(
                firstNotNull(ctx.getGroupId(), defaults.getGroupId()),
                firstNotNull(ctx.getArtifactId(), defaults.getArtifactId()),
                firstNotNull(ctx.getVersion(), defaults.getVersion()));

        final File cruDistZip = goals.copyDist(getBuildDirectory(), artifact);

        try
        {
            // We remove one level of root folder from the zip if present
            int nestingLevel = ZipUtils.countNestingLevel(cruDistZip);
            unzip(cruDistZip, appDir.getPath(), nestingLevel > 0 ? 1 : 0);
        }
        catch (final IOException ex)
        {
            throw new MojoExecutionException("Unable to extract application ZIP artifact", ex);
        }

        return appDir;
    }

    protected File getAppDirectory(Product ctx)
    {
        return new File(getBaseDirectory(ctx), ctx.getId() + "-" + ctx.getVersion());
    }

    @Override
    protected final ProductArtifact getTestResourcesArtifact()
    {
          return new ProductArtifact("com.atlassian.fecru", "amps-fecru", "LATEST");
    }

    @Override
    protected Map<String, String> getSystemProperties(final Product ctx)
    {
        return new HashMap<String, String>()
        {{
            put(FISHEYE_INST, getHomeDirectory(ctx).getAbsolutePath());
        }};
    }

    @Override
    protected String getBundledPluginPath(Product ctx)
    {
        return "plugins/bundled-plugins.zip";
    }

    @Override
    protected Collection<? extends ProductArtifact> getDefaultBundledPlugins()
    {
        return Collections.emptySet();
    }

    @Override
    protected Collection<? extends ProductArtifact> getDefaultLibPlugins()
    {
        return Collections.emptySet();
    }

    @Override
    protected final File getUserInstalledPluginsDirectory(File appDir, File homeDir)
    {
        return new File(new File(new File(homeDir, "var"), "plugins"), "user");
    }

    @Override
    protected boolean supportsStaticPlugins()
    {
        return false;
    }

    @Override
    protected final int startApplication(Product ctx, final File app, final File homeDir, Map<String, String> properties) throws MojoExecutionException
    {
        if (ctx.getJvmArgs() == null)
        {
            ctx.setJvmArgs("-Xmx512m -XX:MaxPermSize=160m");
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

            if (waited++ * STARTUP_CHECK_DELAY > ctx.getStartupTimeout())
            {
                throw new MojoExecutionException("FishEye took longer than " + ctx.getStartupTimeout() + "ms to start!");
            }
        }
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

            if (waited++ * STARTUP_CHECK_DELAY > ctx.getShutdownTimeout())
            {
                throw new MojoExecutionException("FishEye took longer than " + ctx.getShutdownTimeout() + "ms to stop!");
            }
        }
    }

    private AntJavaExecutorThread execFishEyeCmd(String bootCommand, final Product ctx) throws MojoExecutionException
    {
        final Map<String, String> properties = mergeSystemProperties(ctx);

        Java java = javaTaskFactory.newJavaTask(
                output(ctx.getOutput()).
                systemProperties(properties).
                jvmArgs(ctx.getJvmArgs()));

        addOverridesToJavaTask(ctx, java);

        Path classpath = java.createClasspath();
        classpath.createPathElement().setLocation(new File(getAppDirectory(ctx), "fisheyeboot.jar"));

        java.setClassname("com.cenqua.fisheye.FishEyeCtl");

        java.createArg().setValue(bootCommand);

        AntJavaExecutorThread javaThread = new AntJavaExecutorThread(java);
        javaThread.start();

        return javaThread;
    }

    /**
     * Add overrides to the Java task before it gets launched. The Studio version of FishEye-Crucible
     * needs to fork the process here.
     *
     * @param ctx
     * @param java
     */
    protected void addOverridesToJavaTask(final Product ctx, Java java)
    {
        // No override necessary
    }

    protected File getBuildDirectory()
    {
        return new File(project.getBuild().getDirectory());
    }

    private String siteUrl(Product ctx)
    {
        return "http://" + ctx.getServer() + ":" + ctx.getHttpPort() + ctx.getContextPath();
    }

    /**
     * The control port is the httpPort with a "1" appended to it //todo doc this
     */
    protected int controlPort(int httpPort)
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

    @Override
    public void cleanupProductHomeForZip(Product product, File homeDirectory) throws MojoExecutionException, IOException
    {
        super.cleanupProductHomeForZip(product, homeDirectory);
        FileUtils.deleteDirectory(new File(homeDirectory, "var/plugins"));
        FileUtils.deleteDirectory(new File(homeDirectory, "cache/plugins"));
    }
}
