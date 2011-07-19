package com.atlassian.plugins.codegen.modules;

import com.atlassian.plugins.codegen.util.CodeTemplateHelper;
import com.atlassian.plugins.codegen.util.PluginXmlHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Author: jdoklovic
 */
public abstract class AbstractPluginModuleCreator<T extends PluginModuleProperties> implements PluginModuleCreator<T> {
    public static final String DEFAULT_I18N_NAME = "atlassian-plugin";
    public static final String FUNC_TEST_PACKAGE = "it";
    public static final String TEST_SUFFIX = "Test";
    public static final String FUNCT_TEST_SUFFIX = "FuncTest";
    public static final String GENERIC_TEMPLATE_PREFIX = "templates/generic/";
    public static final String GENERIC_TEST_TEMPLATE = GENERIC_TEMPLATE_PREFIX + "GenericTest.java.vtl";

    protected CodeTemplateHelper templateHelper;

    protected AbstractPluginModuleCreator() {
        this(new CodeTemplateHelper());
    }

    protected AbstractPluginModuleCreator(CodeTemplateHelper templateHelper) {
        this.templateHelper = templateHelper;
    }

    @Override
    public abstract void createModule(PluginModuleLocation location, T props) throws Exception;



    protected void addModuleToPluginXml(String templatePath, PluginModuleLocation location, PluginModuleProperties props) throws Exception {
        PluginXmlHelper pluginXmlHelper = new PluginXmlHelper(location.getPluginXml());
        pluginXmlHelper.addModuleAsLastChild(templateHelper.getStringFromTemplate(templatePath, props));
        pluginXmlHelper.addI18nResource(DEFAULT_I18N_NAME);
        pluginXmlHelper.savePluginXml();

        createI18nProperties(location, props);
    }

    private void createI18nProperties(PluginModuleLocation location, PluginModuleProperties props) throws Exception {
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

    protected String testClassname(String classname) {
        return classname + TEST_SUFFIX;
    }

    protected String funcTestClassname(String classname) {
        return classname + FUNCT_TEST_SUFFIX;
    }

    protected String funcTestPackageName(String basePackage) {
        return (StringUtils.isBlank(basePackage) ? FUNC_TEST_PACKAGE : FUNC_TEST_PACKAGE + ".") + basePackage;
    }
}
