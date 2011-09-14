package com.atlassian.maven.plugins.amps.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

public class ConfigFileUtils
{
    public static void replace(List<File> files, List<Replacement> replacements, boolean inverted, Log log) throws MojoExecutionException
    {
        for(File file : files)
        {
            replace(file, replacements, inverted, log);
        }
    }

    /**
     * @param cfgFile the file
     * @param replacements the list of keys to replace with values
     * @param inverted if you want to swap values with keys. Be aware that the list is processed in order,
     * so that if 2 keys have the same value, the first key will be chosen. The Replacement records with
     * reversible set to false will not be reversed. Default: false.
     */
    public static void replace(File cfgFile, List<Replacement> replacements, boolean inverted, Log log) throws MojoExecutionException
    {
        if (!cfgFile.exists())
        {
            log.warn(replacements.size() + " replacements were attempted to be made in the following file, but the file doesn't exist: " + cfgFile.getAbsolutePath());
            return;
        }
        try
        {
            String config = FileUtils.readFileToString(cfgFile);
            if (!inverted)
            {
                for (Replacement replacement : replacements)
                {
                    config = config.replace(replacement.getKey(), replacement.getValue());
                }
            }
            else
            {
                for (Replacement replacement : replacements)
                {
                    if (replacement.isReversible())
                    {
                        config = config.replace(replacement.getValue(), replacement.getKey());
                    }
                }
            }
            FileUtils.writeStringToFile(cfgFile, config);
        }
        catch (IOException ex)
        {
            throw new MojoExecutionException("Unable to replace " + cfgFile, ex);
        }
    }

    public static void replaceAll(File cfgFile, String pattern, String replacement) throws MojoExecutionException
    {
        if (!cfgFile.exists())
        {
            return;
        }
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

    /**
     * Represents a replacement in a configuration file or set of files.
     */
    public static class Replacement
    {
        String key;
        String value;
        boolean reversible = true;

        /**
         * Represents a key to be replaced in the configuration files.
         * @param key the key to be replaced. Must not be null.
         * @param value the value to be replaced. Must not be null.
         */
        public Replacement(String key, String value)
        {
            // Ant-like file pattern matching could be implemented if it proves useful.
            super();
            if (key == null)
            {
                throw new IllegalArgumentException("key must not be null");
            }
            if (value == null)
            {
                throw new IllegalArgumentException("value must not be null");
            }
            this.key = key;
            this.value = value;
        }

        /**
         * Represents a key to be replaced in the configuration files.
         * @param key the key to be replaced. Must not be null.
         * @param value the value to be replaced. Must not be null.
         * @param reversible true if the value should be replaced with the key before
         * preparing a snapshot. Default is true. Use false when the value is non-unique,
         * e.g. "%BAMBOO_ENABLED% = true" should not be reversible.
         */
        public Replacement(String key, String value, boolean reversible)
        {
            this(key, value);
            this.reversible = reversible;
        }

        /**
         * @return the key to be replaced. Never null.
         */
        public String getKey()
        {
            return key;
        }

        /**
         * @return the value. Never null.
         */
        public String getValue()
        {
            return value;
        }

        public boolean isReversible()
        {
            return reversible;
        }

        @Override
        public String toString()
        {
            if (reversible)
            {
                return key + " <-> " + value;
            }
            return key + " -> " + value;
        }



    }
}
