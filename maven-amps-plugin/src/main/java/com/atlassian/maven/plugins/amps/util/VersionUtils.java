package com.atlassian.maven.plugins.amps.util;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public class VersionUtils
{
    public static String getVersion()
    {
        InputStream in = null;
        final Properties props = new Properties();
        try
        {
            in = VersionUtils.class.getClassLoader()
                    .getResourceAsStream(
                            "META-INF/maven/com.atlassian.maven.plugins/maven-amps-plugin/pom.properties");
            if (in != null)
            {
                props.load(in);
                return props.getProperty("version");
            }
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
        return "RELEASE";
    }
}
