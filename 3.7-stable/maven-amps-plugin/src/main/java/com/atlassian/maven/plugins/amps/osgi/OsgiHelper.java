package com.atlassian.maven.plugins.amps.osgi;

import org.apache.maven.project.MavenProject;

/**
 *
 */
public class OsgiHelper
{
    public static boolean isAtlassianPlugin(MavenProject project)
    {
        if (project.getOrganization() != null && project.getOrganization().getName() != null)
        {
            return project.getOrganization().getName().contains("Atlassian");
        }
        return false;
    }
}
