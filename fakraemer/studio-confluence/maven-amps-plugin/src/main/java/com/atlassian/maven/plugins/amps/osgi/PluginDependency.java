package com.atlassian.maven.plugins.amps.osgi;

/**
 * Represents a plugin that is required for this plugin to work.  Will be packaged with the plugin in the obr file.
 */
public class PluginDependency
{
    private String groupId, artifactId;

    public PluginDependency() {
    }

    public PluginDependency(final String groupId, final String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof PluginDependency))
        {
            return false;
        }

        PluginDependency that = (PluginDependency) o;

        if (artifactId != null ? !artifactId.equals(that.artifactId) : that.artifactId != null)
        {
            return false;
        }
        if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = groupId != null ? groupId.hashCode() : 0;
        result = 31 * result + (artifactId != null ? artifactId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return new StringBuilder(groupId).append(":").append(artifactId).toString();
    }
}
