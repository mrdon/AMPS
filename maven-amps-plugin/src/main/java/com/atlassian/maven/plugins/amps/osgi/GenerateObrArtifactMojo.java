package com.atlassian.maven.plugins.amps.osgi;

import com.atlassian.maven.plugins.amps.AbstractAmpsMojo;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.project.MavenProjectHelper;
import org.apache.maven.project.MavenProject;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.artifact.Artifact;
import org.apache.commons.io.FileUtils;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;
import org.jfrog.maven.annomojo.annotations.MojoComponent;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.jar.ManifestException;
import org.codehaus.plexus.archiver.ArchiverException;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;
import java.io.File;
import java.io.IOException;

/**
 * Generates the obr artifact, containing the plugin, its dependencies, and the obr XML file.  The OBR file looks like
 * this:
 *
 * <pre>
 * this-plugin.jar
 * obr.xml
 * dependencies/required-plugin.jar
 * </pre>
 *
 * All plugins in the root directory will be installed, while the ones in the "dependencies" directory will be installed
 * only if they are needed.
 */
@MojoGoal("generate-obr-artifact")
public class GenerateObrArtifactMojo extends AbstractAmpsMojo
{
    @MojoParameter
    private List<PluginDependency> pluginDependencies = new ArrayList<PluginDependency>();

    /**
     * The Jar archiver.
     */
    @MojoComponent(role="org.codehaus.plexus.archiver.Archiver", roleHint="jar")
    private JarArchiver jarArchiver;

    /**
     * The archive configuration to use. See <a href="http://maven.apache.org/shared/maven-archiver/index.html">Maven
     * Archiver Reference</a>.
     *
     * @since 2.1
     */
    @MojoParameter
    private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();

    /**
     * Specifies whether or not to attach the artifact to the project
     */
    @MojoParameter(expression="${attach}", defaultValue="true")
    private boolean attach;

    /**
     * Used for attaching the source jar to the project.
     */
    @MojoComponent
    private MavenProjectHelper projectHelper;

    /**
     * The directory where the generated archive file will be put.
     */
    @MojoParameter(defaultValue="${project.build.directory}")
    protected File outputDirectory;

    /**
     * The filename to be used for the generated archive file.
     * For the source:jar goal, "-sources" is appended to this filename.
     * For the source:test-jar goal, "-test-sources" is appended.
     */
    @MojoParameter(defaultValue="${project.build.finalName}")
    protected String finalName;

    /**
     * Contains the full list of projects in the reactor.
     */
    @MojoParameter(expression="${reactorProjects}", readonly = true)
    protected List reactorProjects;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            List<File> deps = resolvePluginDependencies();
            File obrDir = layoutObr(deps, new File(project.getBuild().getDirectory(), project.getBuild().getFinalName() + ".jar"));
            generateObrZip(obrDir);
        }
        catch (IOException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void generateObrZip(File obrDir) throws MojoExecutionException
    {
        MavenArchiver archiver = new MavenArchiver();
        archiver.setArchiver(jarArchiver);
        File outputFile = new File(outputDirectory, finalName + "-all.obr");
        try
        {
            archiver.getArchiver().addDirectory(obrDir, "");
            archiver.setOutputFile(outputFile);

            archive.setAddMavenDescriptor(false);

            // todo: be smarter about when this is updated
            archive.setForced(true);

            archiver.createArchive(project, archive);
        }
        catch (IOException e)
        {
            throw new MojoExecutionException("Error creating source archive: " + e.getMessage(), e);
        }
        catch (ArchiverException e)
        {
            throw new MojoExecutionException("Error creating source archive: " + e.getMessage(), e);
        }
        catch (DependencyResolutionRequiredException e)
        {
            throw new MojoExecutionException("Error creating source archive: " + e.getMessage(), e);
        }
        catch (ManifestException e)
        {
            throw new MojoExecutionException("Error creating source archive: " + e.getMessage(), e);
        }

        if (attach)
        {
            projectHelper.attachArtifact(project, getType(), "all", outputFile);
        }
        else
        {
            getLog().info("NOT adding " + getType() + " to attached artifacts list.");
        }


    }

    private File layoutObr(List<File> deps, File mainArtifact) throws IOException, MojoExecutionException
    {
        // create directories
        File obrDir = new File(project.getBuild().getDirectory(), "obr");
        obrDir.mkdir();
        File depDir = new File(obrDir, "dependencies");
        depDir.mkdir();

        // Copy in the dependency plugins for the obr generation
        for (File dep : deps)
        {
            FileUtils.copyFileToDirectory(dep, depDir, true);
        }

        // Generate the obr xml
        File obrXml = new File(obrDir, "obr.xml");
        for (File dep : depDir.listFiles())
        {
            getMavenGoals().generateObrXml(dep, obrXml);
        }

        // Copy in the main artifact
        FileUtils.copyFileToDirectory(mainArtifact, obrDir, true);

        getMavenGoals().generateObrXml(mainArtifact, obrXml);

        return obrDir;
    }

    private List<File> resolvePluginDependencies()
    {
        List<File> deps = new ArrayList<File>();
        for (Artifact artifact : new HashSet<Artifact>(project.getDependencyArtifacts()))
        {
            if (pluginDependencies.contains(new PluginDependency(artifact.getGroupId(), artifact.getArtifactId())))
            {
                deps.add(artifact.getFile());
            }
        }
        return deps;
    }

    protected String getType()
    {
        return "obr";
    }
}
