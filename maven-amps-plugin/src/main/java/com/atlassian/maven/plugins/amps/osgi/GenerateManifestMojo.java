package com.atlassian.maven.plugins.amps.osgi;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.atlassian.maven.plugins.amps.AbstractAmpsMojo;

import com.google.common.collect.ImmutableMap;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

import aQute.lib.osgi.Constants;

import static com.atlassian.maven.plugins.amps.util.FileUtils.file;

@MojoGoal("generate-manifest")
public class GenerateManifestMojo extends AbstractAmpsMojo
{
    /**
     * The BND instructions for the bundle.
     */
    @MojoParameter
    private Map<String, String> instructions = new HashMap<String, String>();

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        final MavenProject project = getMavenContext().getProject();
        
        // The Build-Date manifest attribute is used by the Atlassian licensing framework to determine
        // chronological order of bundle versions.  Its value is in milliseconds since the epoch.
        final String buildDateStr = String.valueOf(new Date().getTime());
        final Map<String, String> basicAttributes = ImmutableMap.of("Build-Date", buildDateStr);
        
        if (!instructions.isEmpty())
        {
            getLog().info("Generating a manifest for this plugin");

            if (!instructions.containsKey(Constants.EXPORT_PACKAGE))
            {
                instructions.put(Constants.EXPORT_PACKAGE, "");
            }

            File metainfLib = file(project.getBuild().getOutputDirectory(), "META-INF", "lib");
            if (metainfLib.exists())
            {
                StringBuilder sb = new StringBuilder(".");
                for (File lib : metainfLib.listFiles())
                {
                    sb.append(",").append("META-INF/lib/" + lib.getName());
                }
                instructions.put(Constants.BUNDLE_CLASSPATH, sb.toString());
            }
            getMavenGoals().generateBundleManifest(instructions, basicAttributes);
        }
        else
        {
            if (OsgiHelper.isAtlassianPlugin(project))
            {
                getLog().warn("Atlassian plugin detected as the organisation name includes the string 'Atlassian'.  If " +
                              "this is meant for production, you should add bundle " +
                              "instructions specifically configuring what packages are imported and exported.  This " +
                              "helps catch manifest generation bugs during the build rather than upon install.  The " +
                              "bundle generation configuration can be specified " +
                              "via the <instructions> element in the maven-" + getPluginInformation().getId()+"-plugin configuration.  For example:\n" +
                              "    <configuration>\n" +
                              "        <Import-Package>\n" +
                              "            com.atlassian.myplugin*,\n" +
                              "            com.library.optional.*;resolution:=optional,\n" +
                              "            *\n" +
                              "        </Import-Package>\n" +
                              "    </configuration>\n\n" +
                              "See the Maven bundle plugin (which is used under the covers) for more info: " +
                              "http://felix.apache.org/site/apache-felix-maven-bundle-plugin-bnd.html#ApacheFelixMavenBundlePlugin%28BND%29-Instructions");
            }
            else
            {
                getLog().info("No manifest instructions found, adding only non-OSGi manifest attributes");
            }
            getMavenGoals().generateMinimalManifest(basicAttributes);
        }
    }
}
