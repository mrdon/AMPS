package com.atlassian.maven.plugins.amps;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.execution.ReactorManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuilderConfiguration;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoParameter;
import org.jfrog.maven.annomojo.annotations.MojoRequiresProject;

import static java.util.Collections.singletonList;

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
            // overall goal here is to create a new MavenContext / MavenGoals for the standalone project
            final MavenContext oldContext = getMavenContext();

            // construct new project for new context
            final MavenProject
                oldProject = oldContext.getProject(),
                newProject = projectBuilder.buildFromRepository(artifact, repositories, localRepository);

            // horrible hack #1: buildFromRepository() doesn't actually set the project's remote repositories
            newProject.setRemoteArtifactRepositories(oldProject.getRemoteArtifactRepositories());
            newProject.setPluginArtifactRepositories(oldProject.getPluginArtifactRepositories());

            // horrible hack #2: we need to modify the session to use the new project as its reactor
            final List<MavenProject> newReactor = singletonList(newProject);
            final MavenSession
                oldSession = oldContext.getSession(),
                newSession = new MavenSession(
                    oldSession.getContainer(),
                    oldSession.getSettings(),
                    oldSession.getLocalRepository(),
                    oldSession.getEventDispatcher(),
                    new ReactorManager(newReactor),
                    oldSession.getGoals(),
                    oldSession.getExecutionRootDirectory(),
                    oldSession.getExecutionProperties(),
                    oldSession.getUserProperties(),
                    oldSession.getStartTime()
                );

            // horrible hack #3: we need to create a base directory from scratch, and convince the project to like it
            final String baseDir = System.getProperty("user.dir") + "/amps-standalone/";
            newProject.setFile(new File(baseDir, "pom.xml"));

            ProjectBuilderConfiguration projectBuilderConfiguration =
                    getProjectBuilderConfigurationFromMavenSession(newSession);

            projectBuilder.calculateConcreteState(newProject, projectBuilderConfiguration);

            // finally, execute run goal against standalone project
            final MavenContext newContext = oldContext.with(
                newProject,
                newReactor,
                newSession);
            new MavenGoals(newContext).executeAmpsRecursively(version, "run");
        }
        catch (Exception e)
        {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    /**
     * MavenSession.getProjectBuilderConfiguration() works at runtime with Maven 2 but isn't
     * available at compile time when we build with Maven 3 artifacts.
     */
    private static ProjectBuilderConfiguration getProjectBuilderConfigurationFromMavenSession(MavenSession session)
        throws MojoExecutionException, InvocationTargetException, IllegalAccessException
    {
        try
        {
            Method m = MavenSession.class.getMethod("getProjectBuilderConfiguration");
            return (ProjectBuilderConfiguration) m.invoke(session);
        }
        catch (NoSuchMethodException e)
        {
            throw new MojoExecutionException("Maven 3 is not supported for run-standalone", e);
        }
    }
}
