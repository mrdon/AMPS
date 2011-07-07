package com.atlassian.plugins.codegen.annotations;

/**
 * @since version
 */
public class DependencyDescriptor {
    private String groupId;
    private String artifactId;
    private String version;
    private String scope;

    public DependencyDescriptor() {
        this.scope = "";
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DependencyDescriptor that = (DependencyDescriptor) o;

        if (!artifactId.equals(that.artifactId)) return false;
        if (!groupId.equals(that.groupId)) return false;
        if (!scope.equals(that.scope)) return false;
        if (!version.equals(that.version)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = groupId.hashCode();
        result = 31 * result + artifactId.hashCode();
        result = 31 * result + version.hashCode();
        result = 31 * result + scope.hashCode();
        return result;
    }
}
