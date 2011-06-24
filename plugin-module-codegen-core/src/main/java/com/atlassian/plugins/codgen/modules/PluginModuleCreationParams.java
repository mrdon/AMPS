package com.atlassian.plugins.codgen.modules;

import com.atlassian.plugins.codgen.PluginModuleLocation;

import java.io.File;
import java.util.Properties;

/**
 * Author: jdoklovic
 */
public class PluginModuleCreationParams {
    private static final String KEY_PACKAGE_NAME = "PACKAGE";
    private final File sourceDirectory;
    private final File templateDirectory;
    private final File pluginXml;
    private final Properties properties;

    private PluginModuleCreationParams(File sourceDirectory, File templateDirectory, File pluginXml, Properties properties) {
        this.sourceDirectory = sourceDirectory;
        this.templateDirectory = templateDirectory;
        this.pluginXml = pluginXml;
        this.properties = properties;
    }

    public File getSourceDirectory() {
        return sourceDirectory;
    }

    public File getTemplateDirectory() {
        return templateDirectory;
    }

    public File getPluginXml() {
        return pluginXml;
    }

    public Properties getProperties() {
        return properties;
    }

    public static class Builder {
        private File sourceDirectory;
        private File templateDirectory;
        private File pluginXml;
        private Properties properties;

        public Builder(File sourceDirectory) {
            this.sourceDirectory = sourceDirectory;
        }

        public Builder(PluginModuleLocation location) {
            this.sourceDirectory = location.getSourceDirectory();
            this.templateDirectory = location.getTemplateDirectory();
            this.pluginXml = location.getPluginXml();
        }

        public Builder templateDirectory(File templateDirectory) {
            this.templateDirectory = templateDirectory;
            return this;
        }

        public Builder pluginXml(File pluginXml) {
            this.pluginXml = pluginXml;
            return this;
        }

        public Builder properties(Properties properties) {
            this.properties = properties;
            return this;
        }

        public PluginModuleCreationParams build() {
            return new PluginModuleCreationParams(sourceDirectory, templateDirectory, pluginXml, properties);
        }
    }
}
