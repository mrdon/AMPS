package com.atlassian.maven.plugins.amps.osgi;

import aQute.lib.osgi.Constants;
import com.atlassian.maven.plugins.amps.AbstractAmpsMojo;
import static com.atlassian.maven.plugins.amps.util.FileUtils.*;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Manifest;

/**
 * @goal validate-manifest
 */
public class ValidateManifestMojo extends AbstractAmpsMojo
{
    /**
     * Whether to skip validation or not
     * @parameter expression="${manifest.validation.skip}"
     */
    protected boolean skipManifestValidation = false;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        File mfile = file(getMavenContext().getProject().getBuild().getOutputDirectory(), "META-INF", "MANIFEST.MF");

        // Only valid if the manifest exists
        if (!skipManifestValidation && mfile.exists())
        {
            getLog().info("Manifest found, validating...");
            InputStream mfin = null;
            try
            {
                mfin = new FileInputStream(mfile);
                Manifest mf = new Manifest(mfin);

                PackageImportVersionValidator validator = new PackageImportVersionValidator(getMavenContext().getProject());
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
            getLog().info("No manifest found or validation skip flag specified, skipping validation");
        }
    }
}
