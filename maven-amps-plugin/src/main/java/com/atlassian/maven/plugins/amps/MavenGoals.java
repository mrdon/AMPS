package com.atlassian.maven.plugins.amps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Manifest;
import java.util.regex.Matcher;

import com.atlassian.core.util.FileUtils;
import com.atlassian.maven.plugins.amps.util.PluginXmlUtils;
import com.atlassian.maven.plugins.amps.util.VersionUtils;

import com.sun.jersey.wadl.resourcedoc.ResourceDocletJSON;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.twdata.maven.mojoexecutor.MojoExecutor.Element;
import org.twdata.maven.mojoexecutor.MojoExecutor.ExecutionEnvironment;

import static com.atlassian.maven.plugins.amps.util.FileUtils.file;
import static org.twdata.maven.mojoexecutor.MojoExecutor.artifactId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executeMojo;
import static org.twdata.maven.mojoexecutor.MojoExecutor.goal;
import static org.twdata.maven.mojoexecutor.MojoExecutor.groupId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.name;
import static org.twdata.maven.mojoexecutor.MojoExecutor.plugin;
import static org.twdata.maven.mojoexecutor.MojoExecutor.version;

/**
 * Executes specific maven goals
 */
public class MavenGoals
{
    private final MavenContext ctx;

    private final Log log;
    private final Map<String, String> pluginArtifactIdToVersionMap;

    private final Map<String, Container> idToContainerMap = new HashMap<String, Container>()
    {{
            put("tomcat5x", new Container("tomcat5x", "org.apache.tomcat", "apache-tomcat", "5.5.26"));
            put("tomcat6x", new Container("tomcat6x", "org.apache.tomcat", "apache-tomcat", "6.0.20"));
            put("tomcat7x", new Container("tomcat6x", "org.apache.tomcat", "apache-tomcat", "6.0.20", "windows-x64"));
            put("resin3x", new Container("resin3x", "com.caucho", "resin", "3.0.26"));
            put("jboss42x", new Container("jboss42x", "org.jboss.jbossas", "jbossas", "4.2.3.GA"));
            put("jetty6x", new Container("jetty6x"));
        }};

    private final Map<String, String> defaultArtifactIdToVersionMap = new HashMap<String, String>()
    {{
            put("maven-cli-plugin", "0.7");
            put("cargo-maven2-plugin", "1.0-beta-2-db2");
            // Below is a second definition of 'cargo-maven2-plugin', using CodeHaus instead of TwData.
            put("org.codehaus.cargo:cargo-maven2-plugin", "1.1.3");
            put("atlassian-pdk", "2.3.0");
            put("maven-archetype-plugin", "2.0-alpha-4");
            put("maven-bundle-plugin", "2.0.0");
            put("yuicompressor-maven-plugin", "0.7.1");
            put("build-helper-maven-plugin", "1.7");

            // You can't actually override the version a plugin if defined in the project, so these don't actually do
            // anything, since the super pom already defines versions.
            put("maven-dependency-plugin", "2.0");
            put("maven-resources-plugin", "2.3");
            put("maven-jar-plugin", "2.2");
            put("maven-surefire-plugin", "2.4.3");

        }};

    public MavenGoals(final MavenContext ctx)
    {
        this.ctx = ctx;

        this.log = ctx.getLog();

        this.pluginArtifactIdToVersionMap = Collections.unmodifiableMap(defaultArtifactIdToVersionMap);
    }

    private ExecutionEnvironment executionEnvironment()
    {
        return ctx.getExecutionEnvironment();
    }

    public MavenProject getContextProject()
    {
        return ctx.getProject();
    }

    public void executeAmpsRecursively(final String ampsVersion, final String ampsGoal, Xpp3Dom cfg) throws MojoExecutionException
    {
        executeMojo(
            plugin(
                groupId("com.atlassian.maven.plugins"),
                artifactId("maven-amps-plugin"),
                version(ampsVersion)
            ),
            goal(ampsGoal),
            cfg,
            executionEnvironment());
    }

    public void startCli(final PluginInformation pluginInformation, final int port) throws MojoExecutionException
    {
        final String pluginId = pluginInformation.getId();

        final List<Element> configs = new ArrayList<Element>();
        configs.add(element(name("commands"),
                element(name("pi"),
                        "resources" + " "
                        + "com.atlassian.maven.plugins:maven-" + pluginId + "-plugin:filter-plugin-descriptor" + " "
                        + "compile" + " "
                        + "com.atlassian.maven.plugins:maven-" + pluginId + "-plugin:copy-bundled-dependencies" + " "
                        + "com.atlassian.maven.plugins:maven-" + pluginId + "-plugin:compress-resources" + " "
                        + "com.atlassian.maven.plugins:maven-" + pluginId + "-plugin:generate-manifest" + " "
                        + "com.atlassian.maven.plugins:maven-" + pluginId + "-plugin:validate-manifest" + " "
                        + "com.atlassian.maven.plugins:maven-" + pluginId + "-plugin:jar" + " "
                        + "com.atlassian.maven.plugins:maven-" + pluginId + "-plugin:install"),
                element(name("tpi"),
                        "testResources" + " "
                        + "testCompile" + " "
                        + "com.atlassian.maven.plugins:maven-" + pluginId + "-plugin:test-jar" + " "
                        + "com.atlassian.maven.plugins:maven-" + pluginId + "-plugin:test-install"),
                element(name("package"),
                        "resources" + " "
                        + "com.atlassian.maven.plugins:maven-" + pluginId + "-plugin:filter-plugin-descriptor" + " "
                        + "compile" + " "
                        + "com.atlassian.maven.plugins:maven-" + pluginId + "-plugin:copy-bundled-dependencies" + " "
                        + "com.atlassian.maven.plugins:maven-" + pluginId + "-plugin:generate-manifest" + " "
                        + "com.atlassian.maven.plugins:maven-" + pluginId + "-plugin:jar" + " ")));
        if (port > 0)
        {
            configs.add(element(name("port"), String.valueOf(port)));
        }
        executeMojo(
                plugin(
                        groupId("org.twdata.maven"),
                        artifactId("maven-cli-plugin"),
                        version(pluginArtifactIdToVersionMap.get("maven-cli-plugin"))
                ),
                goal("execute"),
                configuration(configs.toArray(new Element[0])),
                executionEnvironment());
    }

    public void createPlugin(final String productId) throws MojoExecutionException
    {
        executeMojo(
                plugin(
                        groupId("org.apache.maven.plugins"),
                        artifactId("maven-archetype-plugin"),
                        version(defaultArtifactIdToVersionMap.get("maven-archetype-plugin"))
                ),
                goal("generate"),
                configuration(
                        element(name("archetypeGroupId"), "com.atlassian.maven.archetypes"),
                        element(name("archetypeArtifactId"), productId + "-plugin-archetype"),
                        element(name("archetypeVersion"), VersionUtils.getVersion())
                ),
                executionEnvironment());
    }

    public void copyBundledDependencies() throws MojoExecutionException
    {
        executeMojo(
                plugin(
                        groupId("org.apache.maven.plugins"),
                        artifactId("maven-dependency-plugin"),
                        version(defaultArtifactIdToVersionMap.get("maven-dependency-plugin"))
                ),
                goal("copy-dependencies"),
                configuration(
                        element(name("includeScope"), "runtime"),
                        element(name("excludeScope"), "provided"),
                        element(name("excludeScope"), "test"),
                        element(name("includeTypes"), "jar"),
                        element(name("outputDirectory"), "${project.build.outputDirectory}/META-INF/lib")
                ),
                executionEnvironment()
        );
    }

    public void extractBundledDependencies() throws MojoExecutionException
    {
         executeMojo(
                 plugin(
                        groupId("org.apache.maven.plugins"),
                        artifactId("maven-dependency-plugin"),
                        version(defaultArtifactIdToVersionMap.get("maven-dependency-plugin"))
                ),
                goal("unpack-dependencies"),
                configuration(
                        element(name("includeScope"), "runtime"),
                        element(name("excludeScope"), "provided"),
                        element(name("excludeScope"), "test"),
                        element(name("includeTypes"), "jar"),
                        element(name("excludes"), "META-INF/MANIFEST.MF, META-INF/*.DSA, META-INF/*.SF"),
                        element(name("outputDirectory"), "${project.build.outputDirectory}")
                ),
                executionEnvironment()
        );
    }

    public void compressResources() throws MojoExecutionException
    {
        executeMojo(
                plugin(
                        groupId("net.sf.alchim"),
                        artifactId("yuicompressor-maven-plugin"),
                        version(defaultArtifactIdToVersionMap.get("yuicompressor-maven-plugin"))
                ),
                goal("compress"),
                configuration(
                        element(name("suffix"), "-min"),
                        element(name("jswarn"), "false")
                ),
                executionEnvironment()
        );
    }

    public void filterPluginDescriptor() throws MojoExecutionException
    {
        executeMojo(
                plugin(
                        groupId("org.apache.maven.plugins"),
                        artifactId("maven-resources-plugin"),
                        version(defaultArtifactIdToVersionMap.get("maven-resources-plugin"))
                ),
                goal("copy-resources"),
                configuration(
                        element(name("encoding"), "UTF-8"),
                        element(name("resources"),
                                element(name("resource"),
                                        element(name("directory"), "src/main/resources"),
                                        element(name("filtering"), "true"),
                                        element(name("includes"),
                                                element(name("include"), "atlassian-plugin.xml"))
                                )
                        ),
                        element(name("outputDirectory"), "${project.build.outputDirectory}")
                ),
                executionEnvironment()
        );
    }

    public void runUnitTests(Map<String, Object> systemProperties) throws MojoExecutionException
    {
        final Element systemProps = convertPropsToElements(systemProperties);

        executeMojo(
                plugin(
                        groupId("org.apache.maven.plugins"),
                        artifactId("maven-surefire-plugin"),
                        version(defaultArtifactIdToVersionMap.get("maven-surefire-plugin"))
                ),
                goal("test"),
                configuration(
                        systemProps,
                        element(name("excludes"),
                                element(name("exclude"), "it/**"),
                                element(name("exclude"), "**/*$*"))
                ),
                executionEnvironment()
        );
    }

    public File copyWebappWar(final String productId, final File targetDirectory, final ProductArtifact artifact)
            throws MojoExecutionException
    {
        final File webappWarFile = new File(targetDirectory, productId + "-original.war");
        executeMojo(
                plugin(
                        groupId("org.apache.maven.plugins"),
                        artifactId("maven-dependency-plugin"),
                        version(defaultArtifactIdToVersionMap.get("maven-dependency-plugin"))
                ),
                goal("copy"),
                configuration(
                        element(name("artifactItems"),
                                element(name("artifactItem"),
                                        element(name("groupId"), artifact.getGroupId()),
                                        element(name("artifactId"), artifact.getArtifactId()),
                                        element(name("type"), "war"),
                                        element(name("version"), artifact.getVersion()),
                                        element(name("destFileName"), webappWarFile.getName()))),
                        element(name("outputDirectory"), targetDirectory.getPath())
                ),
                executionEnvironment()
        );
        return webappWarFile;
    }

    /**
     * Copies {@code artifacts} to the {@code outputDirectory}. Artifacts are looked up in order: <ol> <li>in the maven
     * reactor</li> <li>in the maven repositories</li> </ol> This can't be used in a goal that happens before the
     * <em>package</em> phase as artifacts in the reactor will be not be packaged (and therefore 'copiable') until this
     * phase.
     *
     * @param outputDirectory the directory to copy artifacts to
     * @param artifacts       the list of artifact to copy to the given directory
     */
    public void copyPlugins(final File outputDirectory, final List<ProductArtifact> artifacts)
            throws MojoExecutionException
    {
        for (ProductArtifact artifact : artifacts)
        {
            final MavenProject artifactReactorProject = getReactorProjectForArtifact(artifact);
            if (artifactReactorProject != null)
            {

                log.debug(artifact + " will be copied from reactor project " + artifactReactorProject);
                final File artifactFile = artifactReactorProject.getArtifact().getFile();
                if (artifactFile == null)
                {
                    log.warn("The plugin " + artifact + " is in the reactor but not the file hasn't been attached.  Skipping.");
                }
                else
                {
                    log.debug("Copying " + artifactFile + " to " + outputDirectory);
                    try
                    {
                        FileUtils.copyFile(artifactFile, new File(outputDirectory, artifactFile.getName()));
                    }
                    catch (IOException e)
                    {
                        throw new MojoExecutionException("Could not copy " + artifact + " to " + outputDirectory, e);
                    }
                }

            }
            else
            {
                executeMojo(
                        plugin(
                                groupId("org.apache.maven.plugins"),
                                artifactId("maven-dependency-plugin"),
                                version(defaultArtifactIdToVersionMap.get("maven-dependency-plugin"))
                        ),
                        goal("copy"),
                        configuration(
                                element(name("artifactItems"),
                                        element(name("artifactItem"),
                                                element(name("groupId"), artifact.getGroupId()),
                                                element(name("artifactId"), artifact.getArtifactId()),
                                                element(name("version"), artifact.getVersion()))),
                                element(name("outputDirectory"), outputDirectory.getPath())
                        ),
                        executionEnvironment());
            }
        }
    }

    private MavenProject getReactorProjectForArtifact(ProductArtifact artifact)
    {
        for (final MavenProject project : ctx.getReactor())
        {
            if (project.getGroupId().equals(artifact.getGroupId())
                    && project.getArtifactId().equals(artifact.getArtifactId())
                    && project.getVersion().equals(artifact.getVersion()))
            {
                return project;
            }
        }
        return null;
    }

    private void unpackContainer(final Container container) throws MojoExecutionException
    {
        executeMojo(
                plugin(
                        groupId("org.apache.maven.plugins"),
                        artifactId("maven-dependency-plugin"),
                        version(defaultArtifactIdToVersionMap.get("maven-dependency-plugin"))
                ),
                goal("unpack"),
                configuration(
                        element(name("artifactItems"),
                                element(name("artifactItem"),
                                        element(name("groupId"), container.getGroupId()),
                                        element(name("artifactId"), container.getArtifactId()),
                                        element(name("version"), container.getVersion()),
                                        element(name("classifier"), container.getClassifier()),
                                        element(name("type"), "zip"))),
                        element(name("outputDirectory"), container.getRootDirectory(getBuildDirectory()))
                ),
                executionEnvironment());
    }

    private String getBuildDirectory()
    {
        return ctx.getProject().getBuild().getDirectory();
    }

    public int startWebapp(final String productInstanceId, final File war, final Map<String, String> systemProperties, final List<ProductArtifact> extraContainerDependencies,
                           final Product webappContext) throws MojoExecutionException
    {
        final Container container = findContainer(webappContext.getContainerId());
        File containerDir = new File(container.getRootDirectory(getBuildDirectory()));

        // retrieve non-embedded containers
        if (!container.isEmbedded())
        {
            if (containerDir.exists())
            {
                log.info("Reusing unpacked container '" + container.getId() + "' from " + containerDir.getPath());
            }
            else
            {
                log.info("Unpacking container '" + container.getId() + "' from container artifact: " + container.toString());
                unpackContainer(container);
            }
        }

        final int rmiPort = pickFreePort(0);
        final int actualHttpPort = pickFreePort(webappContext.getHttpPort());
        final List<Element> sysProps = new ArrayList<Element>();
        if (webappContext.getJvmArgs() == null)
        {
            webappContext.setJvmArgs("-Xmx512m -XX:MaxPermSize=160m");
        }

        for (final Map.Entry<String, String> entry : systemProperties.entrySet())
        {
            webappContext.setJvmArgs(webappContext.getJvmArgs() + " -D" + entry.getKey() + "=\"" + entry.getValue() + "\"");
            sysProps.add(element(name(entry.getKey()), entry.getValue()));
        }
        log.info("Starting " + productInstanceId + " on the " + container.getId() + " container on ports "
                + actualHttpPort + " (http) and " + rmiPort + " (rmi)");

        final String baseUrl = getBaseUrl(webappContext, actualHttpPort);
        sysProps.add(element(name("baseurl"), baseUrl));

        final List<Element> deps = new ArrayList<Element>();
        for (final ProductArtifact dep : extraContainerDependencies)

        {
            deps.add(element(name("dependency"),
                    element(name("location"), webappContext.getArtifactRetriever().resolve(dep))
            ));
        }

        final List<Element> props = new ArrayList<Element>();
        for (final Map.Entry<String, String> entry : systemProperties.entrySet())
        {
            props.add(element(name(entry.getKey()), entry.getValue()));
        }
        props.add(element(name("cargo.servlet.port"), String.valueOf(actualHttpPort)));
        props.add(element(name("cargo.rmi.port"), String.valueOf(rmiPort)));
        props.add(element(name("cargo.jvmargs"), webappContext.getJvmArgs()));

        int startupTimeout = webappContext.getStartupTimeout();
        if (Boolean.FALSE.equals(webappContext.getSynchronousStartup()))
        {
            startupTimeout = 0;
        }

        executeMojo(
                cargo(webappContext),
                goal("start"),
                configuration(
                        element(name("wait"), "false"),
                        element(name("container"),
                                element(name("containerId"), container.getId()),
                                element(name("type"), container.getType()),
                                element(name("home"), container.getInstallDirectory(getBuildDirectory())),
                                element(name("output"), webappContext.getOutput()),
                                element(name("systemProperties"), sysProps.toArray(new Element[sysProps.size()])),
                                element(name("dependencies"), deps.toArray(new Element[deps.size()])),
                                element(name("timeout"), String.valueOf(startupTimeout))
                        ),
                        element(name("configuration"),
                                element(name("home"), container.getConfigDirectory(getBuildDirectory(), productInstanceId)),
                                element(name("type"), "standalone"),
                                element(name("properties"), props.toArray(new Element[props.size()])),
                                element(name("deployables"),
                                        element(name("deployable"),
                                                element(name("groupId"), "foo"),
                                                element(name("artifactId"), "bar"),
                                                element(name("type"), "war"),
                                                element(name("location"), war.getPath()),
                                                element(name("properties"),
                                                        element(name("context"), webappContext.getContextPath())
                                                )
                                        )
                                )
                        )
                ),
                executionEnvironment()
        );
        return actualHttpPort;
    }
    
    public void stopWebapp(final String productId, final String containerId, final Product webappContext) throws MojoExecutionException
    {
        final Container container = findContainer(containerId);

        String actualShutdownTimeout = webappContext.getSynchronousStartup() ? "0" : String.valueOf(webappContext.getShutdownTimeout());
        
        executeMojo(
        cargo(webappContext),
        goal("stop"),
        configuration(
             element(name("container"),
                     element(name("containerId"), container.getId()),
                     element(name("type"), container.getType()),
                     element(name("timeout"), actualShutdownTimeout),
                     // org.codehaus.cargo
                     element(name("home"), container.getInstallDirectory(getBuildDirectory()))
             ),
             element(name("configuration"),
                     // org.twdata.maven
                     element(name("home"), container.getConfigDirectory(getBuildDirectory(), productId))/*,
                     // we don't need that atm. since timeout is 0 for org.codehaus.cargo
                     element(name("properties"), createShutdownPortsPropertiesConfiguration(webappContext)) */
             )
        ),
        executionEnvironment()
        );
    }
    
    /**
     * Cargo waits (org.codehaus.cargo.container.tomcat.internal.AbstractCatalinaInstalledLocalContainer#waitForCompletion(boolean waitForStarting)) for 3 ports, but the AJP and RMI ports may 
     * not be correct (see below), so we configure it to wait on the HTTP port only.
     * 
     * Since we're not configuring the AJP port it defaults to 8009. All the Studio applications are currently using 8009 (by default, since not configured in startWebapp)
     * which means that this port might have been taken by a different application (the container will still come up though, see 
     * "INFO: Port busy 8009 java.net.BindException: Address already in use" in the log). Thus we don't want to wait for it because it might be still open also the container 
     * is shut down. 
     * 
     * The RMI port is randomly chosen (see startWebapp), thus we don't have any information close at hand. As a future optimisation, e.g. when we move away from cargo to let's say
     * Apache's Tomcat Maven Plugin we could retrieve the actualy configuration from the server.xml on shutdown and thus know exactly for what which port to wait until it gets closed.
     * We could do that already in cargo (e.g. container/tomcat6x/<productHome>/conf/server.xml) but that means that we have to support all the containers we are supporting with cargo.
     * 
     * Since the HTTP port is the only one that interests us, we set all three ports to this one when calling stop. But since that may be randomly chosen as well we might be waiting
     * for the wrong port to get closed. Since this is the minor use case, one has to either accept the timeout if the default port is open, or configure product.stop.timeout to 0 in
     * order to skip the wait.
     */
    private Element[] createShutdownPortsPropertiesConfiguration(final Product webappContext)
    {
        final List<Element> properties = new ArrayList<Element>();
        String portUsedToDetermineIfShutdownSucceeded = String.valueOf(webappContext.getHttpPort());
        properties.add(element(name("cargo.servlet.port"), portUsedToDetermineIfShutdownSucceeded));
        properties.add(element(name("cargo.rmi.port"), portUsedToDetermineIfShutdownSucceeded));
        properties.add(element(name("cargo.tomcat.ajp.port"), portUsedToDetermineIfShutdownSucceeded));
        return properties.toArray(new Element[properties.size()]);
    }

    /**
     * Decides whether to use the org.twdata.maven.cargo-maven2-plugin or the org.codehaus.cargo.cargo-maven2-plugin.
     * <p/>
     * The org.twdata.maven plugin is a fork of the org.codehaus.cargo plugin that has been used in AMPS so far. The
     * org.codehaus.cargo plugin in the more recent version has the advantage of setting the timeout to 0. This skips
     * waiting for start/stop of the container in order to perform these operations in parallel.
     * 
     * @param if {@link Product#getSynchronousStartup()} is true, org.twdata.maven.cargo-maven2-plugin is chosen, if it
     *        is false, org.codehaus.cargo.cargo-maven2-plugin is chosen
     */
    private Plugin cargo(Product webappContext)
    {
        if (Boolean.TRUE.equals(webappContext.getSynchronousStartup()))
        {
            return plugin(
                groupId("org.twdata.maven"),
                artifactId("cargo-maven2-plugin"),
                version(pluginArtifactIdToVersionMap.get("cargo-maven2-plugin")));
        }
        else
        {
            return plugin(
                groupId("org.codehaus.cargo"),
                artifactId("cargo-maven2-plugin"),
                version(pluginArtifactIdToVersionMap.get("org.codehaus.cargo:cargo-maven2-plugin")));
        }
    }

    public static String getBaseUrl(Product product, int actualHttpPort)
    {
        return getBaseUrl(product.getServer(), product.getHttpPort(), product.getContextPath());
    }

    private static String getBaseUrl(String server, int actualHttpPort, String contextPath)
    {
        String port = actualHttpPort != 80 ? ":" + actualHttpPort : "";
        server = server.startsWith("http") ? server : "http://" + server;
        if (!contextPath.startsWith("/") && StringUtils.isNotBlank(contextPath))
        {
            contextPath = "/" + contextPath;
        }
        return server + port + contextPath;
    }

    public void runTests(String testGroupId, String containerId, List<String> includes, List<String> excludes, Map<String, Object> systemProperties, final File targetDirectory)
    		throws MojoExecutionException
	{
    	List<Element> includeElements = new ArrayList<Element>(includes.size());
    	for (String include : includes)
    	{
    		includeElements.add(element(name("include"), include));
    	}

        List<Element> excludeElements = new ArrayList<Element>(excludes.size() + 2);
        excludeElements.add(element(name("exclude"), "**/*$*"));
        excludeElements.add(element(name("exclude"), "**/Abstract*"));
        for (String exclude : excludes)
        {
        	excludeElements.add(element(name("exclude"), exclude));
        }

        final String testOutputDir = targetDirectory.getAbsolutePath() + "/" + testGroupId + "/" + containerId + "/surefire-reports";
        final String reportsDirectory = "reportsDirectory";
        systemProperties.put(reportsDirectory, testOutputDir);

        final Element systemProps = convertPropsToElements(systemProperties);


        executeMojo(
                plugin(
                        groupId("org.apache.maven.plugins"),
                        artifactId("maven-surefire-plugin"),
                        version(defaultArtifactIdToVersionMap.get("maven-surefire-plugin"))
                ),
                goal("test"),
                configuration(
                        element(name("includes"),
                        		includeElements.toArray(new Element[includeElements.size()])
                        ),
                        element(name("excludes"),
                                excludeElements.toArray(new Element[excludeElements.size()])
                        ),
                        systemProps,
                        element(name(reportsDirectory), testOutputDir)
                ),
                executionEnvironment()
        );
	}

    /**
     * Converts a map of System properties to maven config elements
     */
    private Element convertPropsToElements(Map<String, Object> systemProperties)
    {
        ArrayList<Element> properties = new ArrayList<Element>();

        // add extra system properties... overwriting any of the hard coded values above.
        for (Map.Entry<String, Object> entry: systemProperties.entrySet())
        {
            properties.add(
                    element(name("property"),
                            element(name("name"), entry.getKey()),
                            element(name("value"), entry.getValue().toString())));
        }

        return element(name("systemProperties"), properties.toArray(new Element[properties.size()]));
    }

    private Container findContainer(final String containerId)
    {
        final Container container = idToContainerMap.get(containerId);
        if (container == null)
        {
            throw new IllegalArgumentException("Container " + containerId + " not supported");
        }
        return container;
    }

    int pickFreePort(final int requestedPort)
    {
        ServerSocket socket = null;
        try
        {
            socket = new ServerSocket(requestedPort);
            return requestedPort > 0 ? requestedPort : socket.getLocalPort();
        }
        catch (final IOException e)
        {
            // happens if the requested port is taken, so we need to pick a new one
            ServerSocket zeroSocket = null;
            try
            {
                zeroSocket = new ServerSocket(0);
                return zeroSocket.getLocalPort();
            }
            catch (final IOException ex)
            {
                throw new RuntimeException("Error opening socket", ex);
            }
            finally
            {
                closeSocket(zeroSocket);
            }
        }
        finally
        {
            closeSocket(socket);
        }
    }

    private void closeSocket(ServerSocket socket)
    {
        if (socket != null)
        {
            try
            {
                socket.close();
            }
            catch (final IOException e)
            {
                throw new RuntimeException("Error closing socket", e);
            }
        }
    }

    public void installPlugin(PdkParams pdkParams)
            throws MojoExecutionException
    {
        final String baseUrl = getBaseUrl(pdkParams.getServer(), pdkParams.getPort(), pdkParams.getContextPath());
        executeMojo(
                plugin(
                        groupId("com.atlassian.maven.plugins"),
                        artifactId("atlassian-pdk"),
                        version(pluginArtifactIdToVersionMap.get("atlassian-pdk"))
                ),
                goal("install"),
                configuration(
                        element(name("pluginFile"), pdkParams.getPluginFile()),
                        element(name("username"), pdkParams.getUsername()),
                        element(name("password"), pdkParams.getPassword()),
                        element(name("serverUrl"), baseUrl),
                        element(name("pluginKey"), pdkParams.getPluginKey())
                ),
                executionEnvironment()
        );
    }

    public void uninstallPlugin(final String pluginKey, final String server, final int port, final String contextPath)
            throws MojoExecutionException
    {
        final String baseUrl = getBaseUrl(server, port, contextPath);
        executeMojo(
                plugin(
                        groupId("com.atlassian.maven.plugins"),
                        artifactId("atlassian-pdk"),
                        version(pluginArtifactIdToVersionMap.get("atlassian-pdk"))
                ),
                goal("uninstall"),
                configuration(
                        element(name("username"), "admin"),
                        element(name("password"), "admin"),
                        element(name("serverUrl"), baseUrl),
                        element(name("pluginKey"), pluginKey)
                ),
                executionEnvironment()
        );
    }

    public void installIdeaPlugin() throws MojoExecutionException
    {
        executeMojo(
                plugin(
                        groupId("org.twdata.maven"),
                        artifactId("maven-cli-plugin"),
                        version(pluginArtifactIdToVersionMap.get("maven-cli-plugin"))
                ),
                goal("idea"),
                configuration(),
                executionEnvironment()
        );
    }

    public File copyDist(final File targetDirectory, final ProductArtifact artifact) throws MojoExecutionException
    {
        return copyZip(targetDirectory, artifact, "test-dist.zip");
    }

    public File copyHome(final File targetDirectory, final ProductArtifact artifact) throws MojoExecutionException
    {
        return copyZip(targetDirectory, artifact, artifact.getArtifactId() + ".zip");
    }

    public File copyZip(final File targetDirectory, final ProductArtifact artifact, final String localName) throws MojoExecutionException
    {
        final File artifactZip = new File(targetDirectory, localName);
        executeMojo(
                plugin(
                        groupId("org.apache.maven.plugins"),
                        artifactId("maven-dependency-plugin"),
                        version(defaultArtifactIdToVersionMap.get("maven-dependency-plugin"))
                ),
                goal("copy"),
                configuration(
                        element(name("artifactItems"),
                                element(name("artifactItem"),
                                        element(name("groupId"), artifact.getGroupId()),
                                        element(name("artifactId"), artifact.getArtifactId()),
                                        element(name("type"), "zip"),
                                        element(name("version"), artifact.getVersion()),
                                        element(name("destFileName"), artifactZip.getName()))),
                        element(name("outputDirectory"), artifactZip.getParent())
                ),
                executionEnvironment()
        );
        return artifactZip;
    }

    public void generateBundleManifest(final Map<String, String> instructions, final Map<String, String> basicAttributes) throws MojoExecutionException
    {
        final List<Element> instlist = new ArrayList<Element>();
        for (final Map.Entry<String, String> entry : instructions.entrySet())
        {
            instlist.add(element(entry.getKey(), entry.getValue()));
        }
        for (final Map.Entry<String, String> entry : basicAttributes.entrySet())
        {
            instlist.add(element(entry.getKey(), entry.getValue()));
        }
        executeMojo(
                plugin(
                        groupId("org.apache.felix"),
                        artifactId("maven-bundle-plugin"),
                        version(defaultArtifactIdToVersionMap.get("maven-bundle-plugin"))
                ),
                goal("manifest"),
                configuration(
                        element(name("supportedProjectTypes"),
                                element(name("supportedProjectType"), "jar"),
                                element(name("supportedProjectType"), "bundle"),
                                element(name("supportedProjectType"), "war"),
                                element(name("supportedProjectType"), "atlassian-plugin")),
                        element(name("instructions"), instlist.toArray(new Element[instlist.size()]))
                ),
                executionEnvironment()
        );
    }

    public void generateMinimalManifest(final Map<String, String> basicAttributes) throws MojoExecutionException
    {
        File metaInf = file(ctx.getProject().getBuild().getOutputDirectory(), "META-INF");
        if (!metaInf.exists())
        {
            metaInf.mkdirs();
        }
        File mf = file(ctx.getProject().getBuild().getOutputDirectory(), "META-INF", "MANIFEST.MF");
        Manifest m = new Manifest();
        m.getMainAttributes().putValue("Manifest-Version", "1.0");
        for (Map.Entry<String, String> entry : basicAttributes.entrySet())
        {
            m.getMainAttributes().putValue(entry.getKey(), entry.getValue());
        }
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(mf);
            m.write(fos);
        }
        catch (IOException e)
        {
            throw new MojoExecutionException("Unable to create manifest", e);
        }
        finally
        {
            IOUtils.closeQuietly(fos);
        }
    }

    public void jarWithOptionalManifest(final boolean manifestExists) throws MojoExecutionException
    {
        Element[] archive = new Element[0];
        if (manifestExists)
        {
            archive = new Element[]{element(name("manifestFile"), "${project.build.outputDirectory}/META-INF/MANIFEST.MF")};
        }

        executeMojo(
                plugin(
                        groupId("org.apache.maven.plugins"),
                        artifactId("maven-jar-plugin"),
                        version(defaultArtifactIdToVersionMap.get("maven-jar-plugin"))
                ),
                goal("jar"),
                configuration(
                        element(name("archive"), archive)
                ),
                executionEnvironment()
        );
    }

    public void jarTests(String finalName) throws MojoExecutionException
    {
        executeMojo(
                plugin(
                        groupId("org.apache.maven.plugins"),
                        artifactId("maven-jar-plugin"),
                        version(defaultArtifactIdToVersionMap.get("maven-jar-plugin"))
                ),
                goal("test-jar"),
                configuration(
                        element(name("finalName"), finalName),
                        element(name("archive"),
                            element(name("manifestFile"), "${project.build.testOutputDirectory}/META-INF/MANIFEST.MF"))
                ),
                executionEnvironment()
        );
    }

    public void generateObrXml(File dep, File obrXml) throws MojoExecutionException
    {
        executeMojo(
                plugin(
                        groupId("org.apache.felix"),
                        artifactId("maven-bundle-plugin"),
                        version(defaultArtifactIdToVersionMap.get("maven-bundle-plugin"))
                ),
                goal("install-file"),
                configuration(
                        element(name("obrRepository"), obrXml.getPath()),

                        // the following three settings are required but not really used
                        element(name("groupId"), "doesntmatter"),
                        element(name("artifactId"), "doesntmatter"),
                        element(name("version"), "doesntmatter"),

                        element(name("packaging"), "jar"),
                        element(name("file"), dep.getPath())

                ),
                executionEnvironment()
        );
    }

    /**
     * Adds the file to the artifacts of this build.
     * The artifact will be deployed using the name and version of the current project,
     * as in if your artifactId is 'MyProject', it will be MyProject-1.0-SNAPSHOT.jar,
     * overriding any artifact created at compilation time.
     *
     * Attached artifacts get installed (at install phase) and deployed (at deploy phase)
     * @param file the file
     * @param type the type of the file, default 'jar'
     */
    public void attachArtifact(File file, String type) throws MojoExecutionException
    {

        executeMojo(
                plugin(
                        groupId("org.codehaus.mojo"),
                        artifactId("build-helper-maven-plugin"),
                        version(defaultArtifactIdToVersionMap.get("build-helper-maven-plugin"))
                ),
                goal("attach-artifact"),
                configuration(
                        element(name("artifacts"),
                                element(name("artifact"),
                                        element(name("file"), file.getAbsolutePath()),
                                        element(name("type"), type)
                                    )
                                )
                        ),
                executionEnvironment());

    }

    public void release(String mavenArgs) throws MojoExecutionException
    {
        String args = "";

        if(StringUtils.isNotBlank(mavenArgs)) {
            args = mavenArgs;
        }

        executeMojo(
                plugin(
                        groupId("org.apache.maven.plugins"),
                        artifactId("maven-release-plugin"),
                        version(defaultArtifactIdToVersionMap.get("maven-release-plugin"))
                ),
                goal("prepare"),
                configuration(
                        element(name("arguments"), args)
                ),
                executionEnvironment()
        );

        executeMojo(
                plugin(
                        groupId("org.apache.maven.plugins"),
                        artifactId("maven-release-plugin"),
                        version(defaultArtifactIdToVersionMap.get("maven-release-plugin"))
                ),
                goal("perform"),
                configuration(
                        element(name("arguments"), args)
                ),
                executionEnvironment()
        );
    }

    public void generateRestDocs() throws MojoExecutionException
    {
        MavenProject prj = ctx.getProject();
        List<String> docletPaths = new ArrayList<String>();
        StringBuffer docletPath = new StringBuffer(":" + prj.getBuild().getOutputDirectory());
        String resourcedocPath = prj.getBuild().getOutputDirectory() + File.separator + "resourcedoc.xml";
        StringBuffer packagesPath = new StringBuffer();
        PluginXmlUtils.PluginInfo pluginInfo = PluginXmlUtils.getPluginInfo(ctx);

        try
        {
            docletPaths.addAll(prj.getCompileClasspathElements());
            docletPaths.addAll(prj.getRuntimeClasspathElements());
            docletPaths.addAll(prj.getSystemClasspathElements());

            for(String path : docletPaths) {
                docletPath.append(File.pathSeparator);
                docletPath.append(path);
            }

        } catch (DependencyResolutionRequiredException e)
        {
            throw new MojoExecutionException("Dependencies must be resolved", e);
        }

        List<PluginXmlUtils.RESTModuleInfo> restModules = PluginXmlUtils.getRestModules(ctx);
        for(PluginXmlUtils.RESTModuleInfo moduleInfo : restModules)
        {
            List<String> packageList = moduleInfo.getPackagesToScan();

            for(String packageToScan : packageList)
            {
                if(packagesPath.length() > 0)
                {
                    packagesPath.append(File.pathSeparator);
                }

                String filePath = prj.getBuild().getSourceDirectory() + File.separator + packageToScan.replaceAll("\\.", Matcher.quoteReplacement(File.separator));
                packagesPath.append(filePath);
            }
        }

        if(!restModules.isEmpty() && packagesPath.length() > 0)
        {
            executeMojo(
                    plugin(
                            groupId("org.apache.maven.plugins"),
                            artifactId("maven-javadoc-plugin"),
                            version("2.4")
                    ),
                    goal("javadoc"),
                    configuration(
                            element(name("maxmemory"),"1024m"),
                            element(name("sourcepath"),packagesPath.toString()),
                            element(name("doclet"), ResourceDocletJSON.class.getName()),
                            element(name("docletPath"), docletPath.toString()),
                            element(name("docletArtifacts"),
                                element(name("docletArtifact"),
                                        element(name("groupId"),"xerces"),
                                        element(name("artifactId"),"xercesImpl"),
                                        element(name("version"),"2.9.1")
                                ),
                                element(name("docletArtifact"),
                                        element(name("groupId"),"commons-lang"),
                                        element(name("artifactId"),"commons-lang"),
                                        element(name("version"),"2.6")
                                )
                            ),
                            element(name("additionalparam"),"-output \"" + resourcedocPath + "\""),
                            element(name("useStandarDocletOptions"),"false")
                    ),
                    executionEnvironment()
            );

            try {

                File userAppDocs = new File(prj.getBuild().getOutputDirectory(),"application-doc.xml");
                if(!userAppDocs.exists())
                {
                    String appDocText = FileUtils.getResourceContent("application-doc.xml");
                    appDocText = StringUtils.replace(appDocText, "${rest.doc.title}", pluginInfo.getName());
                    appDocText = StringUtils.replace(appDocText,"${rest.doc.description}",pluginInfo.getDescription());
                    File appDocFile = new File(prj.getBuild().getOutputDirectory(), "application-doc.xml");

                    FileUtils.saveTextFile(appDocText, appDocFile);
                    log.info("Wrote " + appDocFile.getAbsolutePath());
                }

                File userGrammars = new File(prj.getBuild().getOutputDirectory(),"application-grammars.xml");
                if(!userGrammars.exists())
                {
                    String grammarText = FileUtils.getResourceContent("application-grammars.xml");
                    File grammarFile = new File(prj.getBuild().getOutputDirectory(), "application-grammars.xml");

                    FileUtils.saveTextFile(grammarText, grammarFile);

                    log.info("Wrote " + grammarFile.getAbsolutePath());
                }

            } catch (Exception e)
            {
                throw new MojoExecutionException("Error writing REST application xml files",e);
            }
        }
    }

    private static class Container extends ProductArtifact
    {
        private final String id;
        private final String type;
        private final String classifier;

        /**
         * Installable container that can be downloaded by Maven.
         *
         * @param id         identifier of container, eg. "tomcat5x".
         * @param groupId    groupId of container.
         * @param artifactId artifactId of container.
         * @param version    version number of container.
         */
        public Container(final String id, final String groupId, final String artifactId, final String version)
        {
            super(groupId, artifactId, version);
            this.id = id;
            this.type = "installed";
            this.classifier = "";
        }
        
        /**
         * Installable container that can be downloaded by Maven.
         *
         * @param id         identifier of container, eg. "tomcat5x".
         * @param groupId    groupId of container.
         * @param artifactId artifactId of container.
         * @param version    version number of container.
         * @param classifier classifier of the container.
         */
        public Container(final String id, final String groupId, final String artifactId, final String version, final String classifier)
        {
            super(groupId, artifactId, version);
            this.id = id;
            this.type = "installed";
            this.classifier = classifier;
        }

        /**
         * Embedded container packaged with Cargo.
         *
         * @param id identifier of container, eg. "jetty6x".
         */
        public Container(final String id)
        {
            this.id = id;
            this.type = "embedded";
            this.classifier = "";
        }

        /**
         * @return identifier of container.
         */
        public String getId()
        {
            return id;
        }

        /**
         * @return "installed" or "embedded".
         */
        public String getType()
        {
            return type;
        }

        /**
         * @return classifier the classifier of the ProductArtifact
         */
        public String getClassifier()
        {
            return classifier;
        }

        /**
         * @return <code>true</code> if the container type is "embedded".
         */
        public boolean isEmbedded()
        {
            return "embedded".equals(type);
        }

        /**
         * @param buildDir project.build.directory.
         * @return root directory of the container that will house the container installation and configuration.
         */
        public String getRootDirectory(String buildDir)
        {
            return buildDir + File.separator + "container" + File.separator + getId();
        }

        /**
         * @param buildDir project.build.directory.
         * @return directory housing the installed container.
         */
        public String getInstallDirectory(String buildDir)
        {
            return getRootDirectory(buildDir) + File.separator + getArtifactId() + "-" + getVersion();
        }

        /**
         * @param buildDir  project.build.directory.
         * @param productId product name.
         * @return directory to house the container configuration for the specified product.
         */
        public String getConfigDirectory(String buildDir, String productId)
        {
            return getRootDirectory(buildDir) + File.separator + "cargo-" + productId + "-home";
        }
    }
}
