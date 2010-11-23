package com.atlassian.maven.plugins.amps.util;

import com.atlassian.maven.plugins.amps.MavenContext;

import static com.atlassian.maven.plugins.amps.util.FileUtils.file;

/**
 * Utility methods dealing with Maven projects
 *
 * @since 3.3
 */
public class ProjectUtils
{

    /**
     * @return If the test jar should be built based on atlassian-plugin.xml residing in src/test/resources
     */
    public static boolean shouldDeployTestJar(MavenContext context)
    {
        return file(context.getProject().getBuild().getTestOutputDirectory(), "atlassian-plugin.xml").exists();
    }
}
