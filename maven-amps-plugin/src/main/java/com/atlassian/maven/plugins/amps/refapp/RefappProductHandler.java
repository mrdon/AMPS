package com.atlassian.maven.plugins.amps.refapp;

import com.atlassian.maven.plugins.amps.ProductHandler;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.ProductContext;
import com.atlassian.maven.plugins.amps.util.VersionUtils;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Collections;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

import org.apache.maven.project.MavenProject;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class RefappProductHandler implements ProductHandler
{
    private final MavenGoals goals;
    private final MavenProject project;

    public RefappProductHandler(MavenProject project, MavenGoals goals)
    {
        this.project = project;
        this.goals = goals;
    }

    public String getId()
    {
        return "refapp";
    }

    public int start(ProductContext ctx) throws MojoExecutionException
    {
        // Copy the webapp war to target
        final File webappWar = goals.copyWebappWar(getId(), getBaseDirectory(),
                new ProductArtifact(getArtifact().getGroupId(), getArtifact().getArtifactId(), ctx.getVersion()));

        File homeDir = extractAndProcessHomeDirectory(ctx);

        final File combinedWebappWar = addArtifacts(ctx, homeDir, webappWar);

        return goals.startWebapp(getId(), combinedWebappWar, getSystemProperties(), getExtraContainerDependencies(), ctx);
    }

    public void stop(ProductContext ctx) throws MojoExecutionException
    {
        goals.stopWebapp(getId(), ctx.getContainerId());
    }

    public ProductArtifact getArtifact()
    {
        return new ProductArtifact("com.atlassian.refapp", "atlassian-refapp", VersionUtils.getVersion());
    }

    public ProductArtifact getTestResourcesArtifact()
    {
        return null;
    }

    public int getDefaultHttpPort()
    {
        return 5990;
    }

    public Map<String, String> getSystemProperties()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("refapp.home", getHomeDirectory(project).getPath());
        map.put("osgi.cache", "${project.build.directory}/osgi-cache");
        return map;
    }

    protected File getBaseDirectory()
    {
        File dir = new File(project.getBuild().getDirectory(), getId());
        dir.mkdir();
        return dir;
    }


    public File getHomeDirectory(final MavenProject project)
    {
        File homeDir = new File(new File(project.getBuild().getDirectory(), getId()), "refapp-home");
        // Make sure it exists
        if (!homeDir.exists())
        {
            homeDir.mkdirs();
        }
        return homeDir;
    }

    private List<ProductArtifact> getPluginsArtifacts(ProductContext ctx)
    {
        final List<ProductArtifact> artifacts = new ArrayList<ProductArtifact>();
        artifacts.addAll(getDefaultPlugins());
        artifacts.addAll(ctx.getPluginArtifacts());

        return artifacts;
    }

    protected File addArtifacts(final ProductContext ctx, File homeDir, final File webappWar) throws MojoExecutionException
    {
        try
        {
            final String webappDir = new File(getBaseDirectory(), "webapp").getAbsolutePath();
            if (!new File(webappDir).exists())
                unzip(webappWar, webappDir);

            final File pluginsDir = getPluginsDirectory(webappDir, homeDir);
            final File bundledPluginsDir = new File(getBaseDirectory(), "bundled-plugins");

            bundledPluginsDir.mkdir();
            // add bundled plugins
            final File bundledPluginsZip = new File(webappDir, getBundledPluginPath());
            if (bundledPluginsZip.exists()) {
                unzip(bundledPluginsZip, bundledPluginsDir.getPath());
            }

            if (pluginsDir != null)
            {
                // add this plugin itself
                addThisPluginToDirectory(pluginsDir);
                // add plugins2 plugins
                addArtifactsToDirectory(goals, getPluginsArtifacts(ctx), pluginsDir);
            }
            else
            {
                // add this plugin itself
                addThisPluginToDirectory(bundledPluginsDir);
                // add plugins2 plugins
                addArtifactsToDirectory(goals, getPluginsArtifacts(ctx), bundledPluginsDir);
            }

            // add plugins1 plugins

            List<ProductArtifact> artifacts = new ArrayList<ProductArtifact>();
            artifacts.addAll(getDefaultLibPlugins());
            artifacts.addAll(ctx.getLibArtifacts());
            addArtifactsToDirectory(goals, artifacts, new File(webappDir, "WEB-INF/lib"));

            artifacts = new ArrayList<ProductArtifact>();
            artifacts.addAll(getDefaultBundledPlugins());
            artifacts.addAll(ctx.getBundledArtifacts());

            addArtifactsToDirectory(goals, artifacts, bundledPluginsDir);

            if (bundledPluginsDir.list().length > 0)
            {
                com.atlassian.core.util.FileUtils.createZipFile(bundledPluginsDir,bundledPluginsZip);
            }

            // add log4j.properties file if specified
            if (ctx.getLog4jProperties() != null)
            {
                FileUtils.copyFile(ctx.getLog4jProperties(), new File(webappDir, "WEB-INF/classes/log4j.properties"));
            }

            final File warFile = new File(webappWar.getParentFile(), getId() + ".war");
            com.atlassian.core.util.FileUtils.createZipFile(new File(webappDir), warFile);
            return warFile;

        }
        catch (final Exception e)
        {
            e.printStackTrace();
            throw new MojoExecutionException(e.getMessage());
        }
    }

    public void unzip(final File zipFile, final String destDir) throws IOException
    {
        final ZipFile zip = new ZipFile(zipFile);
        final Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements())
        {
            final ZipEntry zipEntry = entries.nextElement();
            final File file = new File(destDir + "/" + zipEntry.getName());
            if (zipEntry.isDirectory())
            {
                file.mkdirs();
                continue;
            }
            InputStream is = null;
            OutputStream fos = null;
            try
            {
                is = zip.getInputStream(zipEntry);
                fos = new FileOutputStream(file);
                IOUtils.copy(is, fos);
            }
            finally
            {
                IOUtils.closeQuietly(is);
                IOUtils.closeQuietly(fos);
            }
        }
    }

    protected File extractAndProcessHomeDirectory(ProductContext ctx) throws MojoExecutionException
    {
        if (getTestResourcesArtifact() != null)
        {

            final File outputDir = getBaseDirectory();
            final File confHomeZip = goals.copyHome(outputDir,
                    new ProductArtifact(
                            getTestResourcesArtifact().getGroupId(),
                            getTestResourcesArtifact().getArtifactId(),
                            ctx.getTestResourcesVersion()));
            final File tmpDir = new File(getBaseDirectory(), "tmp-resources");
            tmpDir.mkdir();

            try
            {
                unzip(confHomeZip, tmpDir.getPath());
                FileUtils.copyDirectory(tmpDir.listFiles()[0],
                        outputDir);
            }
            catch (IOException ex)
            {
                throw new MojoExecutionException("Unable to copy home directory", ex);
            }
            File homeDir = new File(outputDir, tmpDir.listFiles()[0].listFiles()[0].getName());
            processHomeDirectory(project, homeDir);
            return homeDir;
        }
        else
        {
            return getHomeDirectory(project);
        }
    }


    protected String getVersion(ProductContext ctx)
    {
        return (ctx.getVersion() == null ? getArtifact().getVersion() : ctx.getVersion());
    }

    private void addThisPluginToDirectory(final File pluginsDir) throws IOException
    {
        // add the plugin jar to the directory
        final File thisPlugin = getPluginFile();
        FileUtils.copyFile(thisPlugin, new File(pluginsDir, thisPlugin.getName()));
    }

    private File getPluginFile()
    {
        return new File(project.getBuild().getDirectory(), project.getBuild().getFinalName() + ".jar");
    }

    private void addArtifactsToDirectory(final MavenGoals goals, final List<ProductArtifact> artifacts,
                                         final File pluginsDir) throws MojoExecutionException
    {
        // first remove plugins from the webapp that we want to update
        if (pluginsDir.isDirectory() && pluginsDir.exists())
        {
            for (final Iterator<?> iterateFiles = FileUtils.iterateFiles(pluginsDir, null, false); iterateFiles.hasNext();)
            {
                final File file = (File) iterateFiles.next();
                for (final ProductArtifact webappArtifact : artifacts)
                {
                    if (!file.isDirectory() && file.getName()
                            .contains(webappArtifact.getArtifactId()))
                    {
                        file.delete();
                    }
                }
            }
        }
        // copy the all the plugins we want in the webapp
        if (!artifacts.isEmpty())
        {
            goals.copyPlugins(pluginsDir, artifacts);
        }
    }

    public File getPluginsDirectory(final String webappDir, File homeDir)
    {
        return new File(webappDir, "WEB-INF/plugins");
    }

    public List<ProductArtifact> getExtraContainerDependencies()
    {
        return Collections.emptyList();
    }

    public String getBundledPluginPath()
    {
        return "WEB-INF/classes/atlassian-bundled-plugins.zip";
    }

    public void processHomeDirectory(MavenProject project, File homeDir) throws MojoExecutionException
    {
    }

    public List<ProductArtifact> getDefaultPlugins()
    {
        return Collections.emptyList();
    }

    public List<ProductArtifact> getDefaultLibPlugins()
    {
        return Collections.emptyList();
    }

    public List<ProductArtifact> getDefaultBundledPlugins()
    {
        return Collections.emptyList();
    }
}
