package com.atlassian.maven.plugins.amps.osgi;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.jar.Manifest;

import static com.atlassian.maven.plugins.amps.util.FileUtils.file;
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

    /**
     * Whether to skip validation or not
     *
     * @parameter expression="${manifest.validation.skip}"
     */
    protected boolean skipManifestValidation = false;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        File mfile = file(project.getBuild().getOutputDirectory(), "META-INF", "MANIFEST.MF");

        if (!skipManifestValidation)
        {
            // Only valid if the manifest exists
            if (mfile.exists())
            {
                getLog().info("Manifest found, validating...");
                InputStream mfin = null;
                try
                {
                    mfin = new FileInputStream(mfile);
                    Manifest mf = new Manifest(mfin);

                    PackageImportVersionValidator validator = new PackageImportVersionValidator(project);
                    validator.validate(mf.getMainAttributes().getValue(Constants.IMPORT_PACKAGE));
                }
                catch (IOException e)
                {
                    throw new MojoExecutionException("Unable to read manifest", e);
                }
                finally
                {
                    IOUtils.closeQuietly(mfin);
                }
                getLog().info("Manifest validated");
            }
            else
            {
                throw new MojoFailureException("No manifest found to validate.");
            }
        }
        else
        {
            getLog().info("Manifest valiation skip flag specified, skipping validation");
        }
    }
}
