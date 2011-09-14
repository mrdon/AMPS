package com.atlassian.maven.plugins.amps.util;

import java.io.File;

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

    /**
     * Returns the first non null value. Use this to default values.
     * @return the first non null value of values, or null if all values are null
     */
    public static <T> T firstNotNull(T... values)
    {
        for (T value : values)
        {
            if (value != null)
            {
                return value;
            }
        }
        return null;
    }

    public final static File createDirectory(File dir)
    {
        if (!dir.exists() && !dir.mkdirs())
        {
            throw new RuntimeException("Failed to create directory " + dir.getAbsolutePath());
        }
        return dir;
    }
}
