package com.atlassian.maven.plugins.refapp.util;

import com.atlassian.maven.plugins.refapp.WebappArtifact;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.List;

public class ArtifactRetriever
{
    private final ArtifactResolver artifactResolver;
    private final ArtifactFactory artifactFactory;
    private final ArtifactRepository localRepository;
    private final List<ArtifactRepository> remoteRepositories;

    public ArtifactRetriever(ArtifactResolver artifactResolver, ArtifactFactory artifactFactory, ArtifactRepository localRepository, List<ArtifactRepository> remoteRepositories)
    {
        this.artifactResolver = artifactResolver;
        this.artifactFactory = artifactFactory;
        this.localRepository = localRepository;
        this.remoteRepositories = remoteRepositories;
    }

    public String resolve(WebappArtifact dependency) throws MojoExecutionException
    {
        Artifact artifact = this.artifactFactory.createArtifact(dependency.getGroupId(),
        dependency.getArtifactId(), dependency.getVersion(), "compile", "jar");
        try
        {
            this.artifactResolver.resolve(artifact, remoteRepositories, localRepository);
        }
        catch (ArtifactResolutionException e)
        {
            throw new MojoExecutionException("Cannot resolve artifact", e);
        }
        catch (ArtifactNotFoundException e)
        {
            throw new MojoExecutionException("Cannot find artifact", e);
        }
        return artifact.getFile().getPath();
    }
}
