package com.atlassian.maven.plugins.amps;

import java.io.File;
import java.util.Collections;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;
import org.jfrog.maven.annomojo.annotations.MojoRequiresProject;

/**
 * Run the webapp without a plugin project
 */
@MojoGoal ("run-standalone")
@MojoRequiresProject (false)
public class RunStandaloneMojo extends AbstractProductHandlerMojo
{
    private final String
        GROUP_ID = "com.atlassian.amps",
        ARTIFACT_ID = "standalone";

    @MojoParameter (expression = "${component.org.apache.maven.project.MavenProjectBuilder}", required = true, readonly = true)
    private MavenProjectBuilder projectBuilder;

    @SuppressWarnings("unchecked")
    protected void doExecute() throws MojoExecutionException, MojoFailureException
    {
        final String version = getPluginInformation().getVersion();
        final Artifact artifact = artifactFactory.createProjectArtifact(GROUP_ID, ARTIFACT_ID, version);
        try
        {
            // set up base directory for AMPS output
            final String baseDir = System.getProperty("user.dir") + "/amps-standalone/";
            final MavenProject project = projectBuilder.buildFromRepository(artifact, repositories, localRepository);
            project.setBasedir(new File(baseDir));

            // fool build into thinking it lives in that base directory
            final Build build = project.getBuild();
            build.setSourceDirectory(baseDir + "src/main/java");
            build.setScriptSourceDirectory(baseDir + "src/main/scripts");
            build.setTestSourceDirectory(baseDir + "src/test/java");
            build.setOutputDirectory(baseDir + "target/classes");
            build.setTestOutputDirectory(baseDir + "target/test-classes");
            build.setDirectory(baseDir + "target");

            // execute run goal against standalone project
            final MavenGoals goals = getMavenGoals().changeContext(project, Collections.singletonList(project));
            goals.executeAmpsRecursively(version, "run");
        }
        catch (Exception e)
        {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
