package com.atlassian.plugins.codgen;

import java.io.File;

/**
 * Author: jdoklovic
 */
public class PluginModuleLocation {
    private final File sourceDirectory;
    private final File resourcesDir;
    private final File testDirectory;
    private final File templateDirectory;
    private final File pluginXml;

    private PluginModuleLocation(File sourceDirectory, File resourcesDirectory, File testDirectory, File templateDirectory) {
        this.sourceDirectory = sourceDirectory;
        this.templateDirectory = templateDirectory;
        this.resourcesDir = resourcesDirectory;
        this.testDirectory = testDirectory;
        this.pluginXml = new File(resourcesDir, "atlassian-plugin.xml");
    }

    public File getSourceDirectory() {
        return sourceDirectory;
    }

    public File getResourcesDir() {
        return resourcesDir;
    }

    public File getTestDirectory() {
        return testDirectory;
    }

    public File getTemplateDirectory() {
        return templateDirectory;
    }

    public File getPluginXml() {
        return pluginXml;
    }

    public static class Builder {
        private File sourceDirectory;
        private File resourcesDir;
        private File testDirectory;
        private File templateDirectory;

        public Builder(File sourceDirectory) {
            this.sourceDirectory = sourceDirectory;
        }

        public Builder testDirectory(File testDirectory) {
            this.testDirectory = testDirectory;
            return this;
        }

        public Builder resourcesDirectory(File resourcesDir) {
            this.resourcesDir = resourcesDir;
            return this;
        }

        public Builder templateDirectory(File templateDirectory) {
            this.templateDirectory = templateDirectory;
            return this;
        }

        public PluginModuleLocation build() {
            return new PluginModuleLocation(sourceDirectory, resourcesDir, testDirectory, templateDirectory);
        }
    }
}
