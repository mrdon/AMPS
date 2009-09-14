package com.atlassian.maven.plugins.amps.product;

import com.atlassian.maven.plugins.amps.MavenGoals;
import com.atlassian.maven.plugins.amps.ProductArtifact;
import org.apache.maven.project.MavenProject;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.commons.io.FileUtils;
import static org.apache.commons.io.FileUtils.iterateFiles;
import static org.apache.commons.io.FileUtils.copyFile;

import java.io.IOException;
import java.io.File;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractProductHandler implements ProductHandler
{
    protected final MavenGoals goals;
    protected final MavenProject project;

    protected AbstractProductHandler(MavenProject project, MavenGoals goals)
    {
        this.project = project;
        this.goals = goals;
    }

    protected boolean isStaticPlugin() throws IOException
    {
        final File atlassianPluginXml = new File(project.getBasedir(), "src/main/resources/atlassian-plugin.xml");
        if (atlassianPluginXml.exists())
        {
            String text = FileUtils.readFileToString(atlassianPluginXml);
            return !text.contains("pluginsVersion=\"2\"") && !text.contains("plugins-version=\"2\"");
        }
        else
        {
            // probably an osgi bundle
            return false;
        }
    }

    protected void addThisPluginToDirectory(final File targetDir) throws IOException
    {
        final File thisPlugin = getPluginFile();

        // remove any existing version
        for (final Iterator<?> iterateFiles = iterateFiles(targetDir, null, false); iterateFiles.hasNext();)
        {
            final File file = (File) iterateFiles.next();
            if (file.getName().contains(project.getArtifactId()))
            {
                file.delete();
            }
        }

        // add the plugin jar to the directory
        copyFile(thisPlugin, new File(targetDir, thisPlugin.getName()));
    }

    protected File getPluginFile()
    {
        return new File(project.getBuild().getDirectory(), project.getBuild().getFinalName() + ".jar");
    }

    protected void addArtifactsToDirectory(final List<ProductArtifact> artifacts, final File pluginsDir) throws MojoExecutionException
    {
        // first remove plugins from the webapp that we want to update
        if (pluginsDir.isDirectory() && pluginsDir.exists())
        {
            for (final Iterator<?> iterateFiles = FileUtils.iterateFiles(pluginsDir, null, false); iterateFiles.hasNext();)
            {
                final File file = (File) iterateFiles.next();
                for (final ProductArtifact webappArtifact : artifacts)
                {
                    if (!file.isDirectory() && file.getName().contains(webappArtifact.getArtifactId()))
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

    protected File getHomeDirectory()
    {
        File homeDir = new File(new File(project.getBuild().getDirectory(), getId()), "home");
        // Make sure it exists
        if (!homeDir.exists())
        {
            homeDir.mkdirs();
        }
        return homeDir;
    }

}
