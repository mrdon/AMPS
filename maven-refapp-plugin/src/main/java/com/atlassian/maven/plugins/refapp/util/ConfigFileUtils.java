package com.atlassian.maven.plugins.refapp.util;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.IOException;

public class ConfigFileUtils
{
    public static void replace(File cfgFile, String pattern, String replacement) throws MojoExecutionException
    {
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
}
