package com.atlassian.maven.plugins.amps.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;

public class ConfigFileUtils
{
    public static void replace(File cfgFile, String pattern, String replacement) throws MojoExecutionException
    {
        if (!cfgFile.exists())
        {
            return;
        }
        try
        {
            String config = FileUtils.readFileToString(cfgFile);
            config = config.replace(pattern, replacement);
            FileUtils.writeStringToFile(cfgFile, config);
        }
        catch (IOException ex)
        {
            throw new MojoExecutionException("Unable to replace " + cfgFile, ex);
        }
    }

    public static void replaceAll(File cfgFile, String pattern, String replacement) throws MojoExecutionException
    {
        try
        {
            String config = FileUtils.readFileToString(cfgFile);
            config = config.replaceAll(pattern, replacement); // obeys regex
            FileUtils.writeStringToFile(cfgFile, config);
        }
        catch (IOException ex)
        {
            throw new MojoExecutionException("Unable to replace " + cfgFile, ex);
        }
    }

    public static void setProperties(File propertiesFile, Map<String, String> newProperties) throws MojoExecutionException
    {
        InputStream in = null;
        OutputStream out = null;

        try
        {
            in = new FileInputStream(propertiesFile);

            Properties props = new Properties();
            props.load(in);
            in.close();
            in = null;

            for (Map.Entry<String, String> e : newProperties.entrySet())
            {
                props.setProperty(e.getKey(), e.getValue());
            }

            out = new FileOutputStream(propertiesFile);
            props.store(out, "Processed by AMPS");
            out.close();
            out = null;
        }
        catch (IOException ioe)
        {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }
}
