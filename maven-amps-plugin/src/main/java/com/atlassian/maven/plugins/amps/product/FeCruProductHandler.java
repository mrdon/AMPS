package com.atlassian.maven.plugins.amps.product;

import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import static com.atlassian.maven.plugins.amps.util.ConfigFileUtils.replace;
import static com.atlassian.maven.plugins.amps.util.ZipUtils.unzip;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;

public class FeCruProductHandler implements ProductHandler
{

    private final MavenProject project;
    private final MavenGoals goals;

    public FeCruProductHandler(MavenProject project, MavenGoals goals)
    {
        this.project = project;
        this.goals = goals;
    }

    public String getId()
    {
        return ProductHandlerFactory.FECRU;
    }

    public int getDefaultHttpPort()
    {
        return 5990;
    }

    public int start(Product ctx) throws MojoExecutionException
    {
        extractAndProcessHomeDirectory(ctx);

        try
        {
            execFishEyeCmd(ctx, getHomeDirectory(), "run");
        }
        catch (Exception e)
        {
            throw new MojoExecutionException("Error starting fisheye.", e);
        }

        return ctx.getHttpPort();
    }

    public void stop(Product ctx) throws MojoExecutionException
    {
        try
        {
            execFishEyeCmd(ctx, getHomeDirectory(), "stop");
        }
        catch (Exception e)
        {
            throw new MojoExecutionException("Failed to stop FishEye/Crucible instance at " + ctx.getServer() + ":" + ctx.getHttpPort());
        }
    }

    private void execFishEyeCmd(Product ctx, File homeDir, String bootCmd)
    {
//        final Java java = (Java) new AntUtils().createAntTask("java");
//        java.setJar(new File(homeDir, "fisheyeboot.jar"));
//        java.createArg().setValue(bootCmd);
//        java.createArg().setValue("--debug");
//
//        if (ctx.getJvmArgs() != null && ctx.getJvmArgs().length() > 0)
//        {
//            java.setJvmargs(ctx.getJvmArgs());
//        }
//
//        java.setFork(true);
//
//        // todo logging
//        // java.setOutput(getContainerOutputLog());
//
//        final Thread antRunner = new AntContainerExecutorThread(java);
//        antRunner.start();

        //getLog().info("Started Fisheye.");
    }

    private File getHomeDirectory()
    {
        return new File(getBuildDirectory(), "fecru-home");
    }

    private File getBuildDirectory()
    {
        return new File(project.getBuild().getDirectory());
    }

    private void extractAndProcessHomeDirectory(Product ctx) throws MojoExecutionException
    {
        final File ampsDistZip = goals.copyHome(getBuildDirectory(),
                new ProductArtifact(
                        "com.atlassian.fecru",
                        "amps-fecru",
                        ctx.getProductDataVersion()));

        final File homeDir = getHomeDirectory();
        try
        {
            unzip(ampsDistZip, homeDir.getPath());
        }
        catch (final IOException ex)
        {
            throw new MojoExecutionException("Unable to copy home directory", ex);
        }

        //setup config.xml, ports, test repos, whatever
        final File configXml = new File(homeDir, "config.xml");
        replace(configXml, "@CONTROL_BIND@", String.valueOf(controlPort(ctx.getHttpPort())));
        replace(configXml, "@HTTP_BIND@", String.valueOf(ctx.getHttpPort()));
        replace(configXml, "@HTTP_CONTEXT@", String.valueOf(ctx.getContextPath()));
        replace(configXml, "@HOME_DIR@", String.valueOf(homeDir.getAbsolutePath()));
    }

    /**
     * The control port is the httpPort with a "1" appended to it //todo doc this
     */
    private int controlPort(int httpPort)
    {
        return httpPort * 10 + 1;
    }
}
