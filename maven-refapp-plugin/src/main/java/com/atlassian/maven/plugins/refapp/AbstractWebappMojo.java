package com.atlassian.maven.plugins.refapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import com.atlassian.maven.plugins.refapp.util.ArtifactRetriever;

/**
 * Base class for webapp mojos
 */
public abstract class AbstractWebappMojo extends AbstractMojo
{
    /**
     * Container to run in
     *
     * @parameter expression="${container}"
     */
    protected String containerId = "tomcat6x";
    /**
     * HTTP port for the servlet containers
     *
     * @parameter expression="${http.port}"
     */
    private int httpPort = 0;

    /**
     * Application context path
     *
     * @parameter expression="${context.path}"
     */
    protected String contextPath;

    /**
     * Application server
     *
     * @parameter expression="${server}" default-value="localhost"
     */
    protected String server;

    /**
     * Webapp version
     *
     * @parameter expression="${webapp.version}"
     */
    protected String webappVersion;

    /**
     * The build directory
     *
     * @parameter expression="${project.build.directory}"
     * @required
     */
    protected File targetDirectory;

    /**
     * The jar name
     *
     * @parameter expression="${project.build.finalName}"
     * @required
     */
    protected String finalName;

    /**
     * JVM arguments to pass to cargo
     *
     * @parameter expression="${jvmargs}"
     */
    protected String jvmArgs = null;

    /**
     * A log4j properties file
     *
     * @parameter
     */
    protected File log4jProperties;

    /**
     * The Maven Project Object
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    /**
     * The Maven Session Object
     *
     * @parameter expression="${session}"
     * @required
     * @readonly
     */
    protected MavenSession session;
    /**
     * The Maven PluginManager Object
     *
     * @component
     * @required
     */
    protected PluginManager pluginManager;

        /**
     * The artifact resolver is used to dynamically resolve JARs that have to be in the embedded
     * container's classpaths. Another solution would have been to statitically define them a
     * dependencies in the plugin's POM. Resolving them in a dynamic manner is much better as only
     * the required JARs for the defined embedded container are downloaded.
     *
     * @component
     */
    private ArtifactResolver artifactResolver;

    /**
     * The local Maven repository. This is used by the artifact resolver to download resolved
     * JARs and put them in the local repository so that they won't have to be fetched again next
     * time the plugin is executed.
     *
     * @parameter expression="${localRepository}"
     */
    private ArtifactRepository localRepository;

    /**
     * The remote Maven repositories used by the artifact resolver to look for JARs.
     *
     * @parameter expression="${project.remoteArtifactRepositories}"
     */
    private List repositories;

    /**
     * The artifact factory is used to create valid Maven
     * {@link org.apache.maven.artifact.Artifact} objects. This is used to pass Maven artifacts to
     * the artifact resolver so that it can download the required JARs to put in the embedded
     * container's classpaths.
     *
     * @component
     */
    private ArtifactFactory artifactFactory;


    /**
     * The test resources version
     *
     * @parameter expression="${testResourcesVersion}" default-value="LATEST"
     */
    protected String testResourcesVersion;

    /**
     * @parameter
     */
    private List<WebappArtifact> pluginArtifacts = new ArrayList<WebappArtifact>();

    /**
     * @parameter
     */
    private List<WebappArtifact> libArtifacts = new ArrayList<WebappArtifact>();

    /**
     * @parameter
     */
    private List<WebappArtifact> bundledArtifacts = new ArrayList<WebappArtifact>();

    /**
     * Comma-delimited list of plugin artifacts in GROUP_ID:ARTIFACT_ID:VERSION form, where version can be
     * ommitted, defaulting to LATEST
     *
     * @parameter expression="${plugins}
     */
    private String pluginArtifactsString;

    /**
     * Comma-delimited list of lib artifacts in GROUP_ID:ARTIFACT_ID:VERSION form, where version can be
     * ommitted, defaulting to LATEST
     *
     * @parameter expression="${lib.plugins}
     */
    private String libArtifactsString;

    /**
     * Comma-delimited list of bundled plugin artifacts in GROUP_ID:ARTIFACT_ID:VERSION form, where version can be
     * ommitted, defaulting to LATEST
     *
     * @parameter expression="${bundled.plugins}
     */
    private String bundledArtifactsString;

    /**
     * SAL version
     *
     * @parameter expression="${sal.version}
     */
    private String salVersion;

    /**
     * Atlassian Plugin Development Kit (PDK) version
     *
     * @parameter expression="${pdk.version}
     */
    private String pdkVersion;

    /**
     * Atlassian REST module version
     *
     * @parameter expression="${rest.version}
     */
    private String restVersion;

    /**
     * Atlassian REST Plugin Manager version
     *
     * @parameter expression="${rpm.version}
     */
    private String rpmVersion;


    public AbstractWebappMojo()
    {
        if (contextPath == null)
        {
            contextPath = "/" + getWebappHandler().getId();
        }
    }

    protected File getBaseDirectory()
    {
        File dir = new File(project.getBuild().getDirectory(), getWebappHandler().getId());
        dir.mkdir();
        return dir;
    }

    private List<WebappArtifact> getPluginsArtifacts()
    {
        final List<WebappArtifact> artifacts = new ArrayList<WebappArtifact>();
        artifacts.addAll(getWebappHandler().getDefaultPlugins());
        artifacts.addAll(pluginArtifacts);
        if (salVersion != null)
        {
            artifacts.addAll(getWebappHandler().getSalArtifacts(salVersion));
        }

        if (pdkVersion != null)
        {
            artifacts.add(new WebappArtifact("com.atlassian.pdkinstall", "pdkinstall-plugin", pdkVersion));
        }

        if (restVersion != null)
        {
            artifacts.add(new WebappArtifact("com.atlassian.plugins.rest", "atlassian-rest-module", restVersion));
        }

        if (rpmVersion != null)
        {
            artifacts.add(new WebappArtifact("com.atlassian.plugins.rest", "atlassian-rest-plugin-manager-plugin",
                    rpmVersion));
        }

        return artifacts;
    }

    protected File addArtifacts(final MavenGoals goals, File homeDir, final File webappWar) throws MojoExecutionException
    {
        try
        {
            final String webappDir = new File(getBaseDirectory(), "webapp").getAbsolutePath();
            if (!new File(webappDir).exists())
                unzip(webappWar, webappDir);

            final File pluginsDir = getWebappHandler().getPluginsDirectory(webappDir, homeDir);
            final File bundledPluginsDir = new File(getBaseDirectory(), "bundled-plugins");

            bundledPluginsDir.mkdir();
            // add bundled plugins
            final File bundledPluginsZip = new File(webappDir, getWebappHandler().getBundledPluginPath());
            if (bundledPluginsZip.exists()) {
                unzip(bundledPluginsZip, bundledPluginsDir.getPath());
            }

            if (pluginsDir != null)
            {
                // add this plugin itself
                addThisPluginToDirectory(pluginsDir);
                // add plugins2 plugins
                addArtifactsToDirectory(goals, getPluginsArtifacts(), pluginsDir);
            }
            else
            {
                // add this plugin itself
                addThisPluginToDirectory(bundledPluginsDir);
                // add plugins2 plugins
                addArtifactsToDirectory(goals, getPluginsArtifacts(), bundledPluginsDir);
            }

            // add plugins1 plugins

            List<WebappArtifact> artifacts = new ArrayList<WebappArtifact>();
            artifacts.addAll(getWebappHandler().getDefaultLibPlugins());
            artifacts.addAll(libArtifacts);
            addArtifactsToDirectory(goals, artifacts, new File(webappDir, "WEB-INF/lib"));

            artifacts = new ArrayList<WebappArtifact>();
            artifacts.addAll(getWebappHandler().getDefaultBundledPlugins());
            artifacts.addAll(bundledArtifacts);

            addArtifactsToDirectory(goals, artifacts, bundledPluginsDir);

            if (bundledPluginsDir.list().length > 0)
            {
                com.atlassian.core.util.FileUtils.createZipFile(bundledPluginsDir,bundledPluginsZip);
            }

            // add log4j.properties file if specified
            if (log4jProperties != null)
            {
                FileUtils.copyFile(log4jProperties, new File(webappDir, "WEB-INF/classes/log4j.properties"));
            }

            final File warFile = new File(webappWar.getParentFile(), getWebappHandler().getId() + ".war");
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

    protected File extractAndProcessHomeDirectory(MavenGoals goals) throws MojoExecutionException
    {
        if (getWebappHandler().getTestResourcesArtifact() != null)
        {

            final File outputDir = getBaseDirectory();
            final File confHomeZip = goals.copyHome(outputDir, getTestResourcesVersion());
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
            getWebappHandler().processHomeDirectory(project, homeDir, this);
            return homeDir;
        }
        else
        {
            return getWebappHandler().getHomeDirectory(project);
        }
    }

    protected String getVersion()
    {
        return (webappVersion == null ? getWebappHandler().getArtifact().getVersion() : webappVersion);
    }

    private void addThisPluginToDirectory(final File pluginsDir) throws IOException
    {
        // add the plugin jar to the directory
        final File thisPlugin = getPluginFile();
        FileUtils.copyFile(thisPlugin, new File(pluginsDir, thisPlugin.getName()));
    }

    private File getPluginFile()
    {
        return new File(project.getBuild().getDirectory(), finalName + ".jar");
    }

    private void addArtifactsToDirectory(final MavenGoals goals, final List<WebappArtifact> artifacts,
                                         final File pluginsDir) throws MojoExecutionException
    {
        // first remove plugins from the webapp that we want to update
        if (pluginsDir.isDirectory() && pluginsDir.exists())
        {
            for (final Iterator<?> iterateFiles = FileUtils.iterateFiles(pluginsDir, null, false); iterateFiles.hasNext();)
            {
                final File file = (File) iterateFiles.next();
                for (final WebappArtifact webappArtifact : artifacts)
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

    protected WebappHandler getWebappHandler()
    {
        return new RefappWebappHandler();
    }

    protected WebappContext createWebappContext(File war)
    {
        WebappContext ctx = new WebappContext();
        ctx.setWebappWar(war);
        ctx.setContainerId(containerId);
        ctx.setServer(server);
        ctx.setHttpPort(getHttpPort());
        ctx.setContextPath(contextPath);
        ctx.setJvmArgs(jvmArgs);
        ctx.setArtifactRetriever(new ArtifactRetriever(artifactResolver, artifactFactory, localRepository, repositories));
        return ctx;
    }

    public MavenProject getProject()
    {
        return project;
    }

    public MavenSession getSession()
    {
        return session;
    }

    public PluginManager getPluginManager()
    {
        return pluginManager;
    }

    public String getTestResourcesVersion()
    {
        return (testResourcesVersion != null ? testResourcesVersion : getWebappHandler().getTestResourcesArtifact().getVersion());
    }

    public int getHttpPort()
    {
        return httpPort == 0 ? getWebappHandler().getDefaultHttpPort() : httpPort;
    }

    public String getContextPath()
    {
        return contextPath;
    }

    public String getServer()
    {
        return server;
    }



    private List<WebappArtifact> stringToArtifactList(String val, List<WebappArtifact> artifacts)
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
            artifacts.add(new WebappArtifact(groupId, artifactId, version));
        }
        return artifacts;
    }

    public final void execute() throws MojoExecutionException, MojoFailureException
    {
        stringToArtifactList(pluginArtifactsString, pluginArtifacts);
        stringToArtifactList(libArtifactsString, libArtifacts);
        stringToArtifactList(bundledArtifactsString, bundledArtifacts);

        doExecute();
    }

    protected abstract void doExecute() throws MojoExecutionException, MojoFailureException;
}
