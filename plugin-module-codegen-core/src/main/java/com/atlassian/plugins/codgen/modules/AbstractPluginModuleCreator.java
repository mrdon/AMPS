package com.atlassian.plugins.codgen.modules;

import com.atlassian.plugins.codgen.PluginModuleLocation;
import com.atlassian.plugins.codgen.util.CodeTemplateHelper;
import com.atlassian.plugins.codgen.util.PluginXmlHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Author: jdoklovic
 */
public abstract class AbstractPluginModuleCreator implements PluginModuleCreator {
    public static final String DEFAULT_I18N_NAME = "atlassian-plugin";
    protected CodeTemplateHelper templateHelper;

    protected AbstractPluginModuleCreator() {
        this(new CodeTemplateHelper());
    }

    protected AbstractPluginModuleCreator(CodeTemplateHelper templateHelper) {
        this.templateHelper = templateHelper;
    }

    @Override
    public abstract void createModule(PluginModuleLocation location, BasicModuleProperties props) throws Exception;

    protected void addModuleToPluginXml(String templatePath, PluginModuleLocation location, BasicModuleProperties props) throws Exception {
        PluginXmlHelper pluginXmlHelper = new PluginXmlHelper(location.getPluginXml());
        pluginXmlHelper.addModuleAsLastChild(templateHelper.getStringFromTemplate(templatePath, props));
        pluginXmlHelper.addI18nResource(DEFAULT_I18N_NAME);
        pluginXmlHelper.savePluginXml();

        createI18nProperties(location, props);
    }

    private void createI18nProperties(PluginModuleLocation location, BasicModuleProperties props) throws Exception {
        if (location.getResourcesDir() != null && !props.getI18nProperties().isEmpty()) {
            File i18nFile = new File(location.getResourcesDir(), DEFAULT_I18N_NAME + ".properties");

            if (!i18nFile.exists()) {
                i18nFile.createNewFile();
            }

            Properties currentProps = new Properties();
            InputStream is = FileUtils.openInputStream(i18nFile);
            currentProps.load(is);
            IOUtils.closeQuietly(is);

            currentProps.putAll(props.getI18nProperties());

            OutputStream os = FileUtils.openOutputStream(i18nFile);
            currentProps.store(os, "");
            IOUtils.closeQuietly(os);

        }
    }
}
