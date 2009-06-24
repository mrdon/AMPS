package com.atlassian.maven.plugins.refapp;

/**
 * Represents a plugin artifact to be retrieved
 */
public class ProductArtifact
{

    /**
     * @parameter
     * @required
     */
    private String groupId;

    /**
     * @parameter
     * @required
     */
    private String artifactId;

    /**
     * @parameter
     * @required
     */
    private String version;

    public ProductArtifact() {
    }

    public ProductArtifact(final String groupId, final String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    public ProductArtifact(final String groupId, final String artifactId, final String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(final String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(final String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    @Override
    public String toString()
    {
        return new StringBuilder(groupId).append(":").append(artifactId).append(":").append(version).toString();
    }
}
