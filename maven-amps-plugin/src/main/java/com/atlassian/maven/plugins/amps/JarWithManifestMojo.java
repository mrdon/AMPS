package com.atlassian.maven.plugins.amps;

import static com.atlassian.maven.plugins.amps.util.FileUtils.*;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;

import java.io.File;

@MojoGoal("jar")
public class JarWithManifestMojo extends AbstractAmpsMojo
{
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        File mf = file(getMavenContext().getProject().getBuild().getOutputDirectory(), "META-INF", "MANIFEST.MF");
        getMavenGoals().jarWithOptionalManifest(mf.exists());
    }
}
