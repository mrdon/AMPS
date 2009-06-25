package com.atlassian.maven.plugins.amps.osgi;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Map;
import java.util.jar.Manifest;

import static com.atlassian.maven.plugins.amps.util.FileUtils.file;
import aQute.libg.header.OSGiHeader;
import aQute.lib.osgi.Constants;

/**
 * @goal validate-manifest
 */
public class ValidateManifestMojo extends AbstractMojo
{
    /**
     * The Maven Project Object
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        File mfile = file(project.getBuild().getOutputDirectory(), "META-INF", "MANIFEST.MF");

        // Only valid if the manifest exists
        if (mfile.exists())
        {
            getLog().info("Manifest found, validating...");
            InputStream mfin = null;
            try
            {
                mfin = new FileInputStream(mfile);
                Manifest mf = new Manifest(mfin);

                validateAllImportsContainRanges(mf.getMainAttributes().getValue(Constants.IMPORT_PACKAGE));
            }
            catch (IOException e)
            {
                throw new MojoExecutionException("Unable to read manifest", e);
            }
            getLog().info("Manifest validated");
        }
        else
        {
            getLog().info("No manifest found, skipping validation");
        }
    }

    void validateAllImportsContainRanges(String imports) throws MojoFailureException
    {
        if (imports != null)
        {
            Map<String,Map<String,String>> pkgImports = OSGiHeader.parseHeader(imports);
            for (Map.Entry<String,Map<String,String>> pkgImport : pkgImports.entrySet())
            {
                String pkg = pkgImport.getKey();
                if (pkgImport.getValue() != null && pkgImport.getValue().size() > 0)
                {
                    Map<String,String> props = pkgImport.getValue();
                    String version = props.get("version");
                    if (version == null || !version.contains(","))
                    {
                        throw new MojoFailureException("The version for the import of package '" + pkg + "' must be an explicit range, " +
                                "was '" + version + "'");
                    }
                }
                else
                {
                    throw new MojoFailureException("The import for package '" + pkg + "' must specify a version range.");
                }
            }
        }
    }
}
