package com.atlassian.plugins.codegen;

import com.atlassian.plugins.codegen.annotations.asm.ModuleCreatorAnnotationParser;
import com.atlassian.plugins.codegen.modules.*;
import com.atlassian.plugins.codegen.modules.common.web.AbstractWebFragmentProperties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.UUID;

/**
 *
 */
public abstract class AbstractCodegenTestCase<T extends PluginModuleProperties> {
    protected File tempDir;
    protected File srcDir;
    protected File testDir;
    protected File resourcesDir;
    protected File templateDir;
    protected File pluginXml;
    protected PluginModuleCreatorRegistry pluginModuleCreatorRegistry;
    protected ModuleCreatorAnnotationParser parser;

    protected PluginModuleLocation moduleLocation;
    protected T props;
    protected PluginModuleCreator creator;

    public void setProps(T props) {
        this.props = props;
    }

    public void setCreator(PluginModuleCreator creator) {
        this.creator = creator;
    }

    public void setModuleLocation(PluginModuleLocation moduleLocation) {
        this.moduleLocation = moduleLocation;
    }

    @Before
    public void setup() throws Exception {

        pluginModuleCreatorRegistry = new PluginModuleCreatorRegistryImpl();
        parser = new ModuleCreatorAnnotationParser(pluginModuleCreatorRegistry);
        parser.parse();

        final File sysTempDir = new File("target");
        String dirName = UUID.randomUUID().toString();
        tempDir = new File(sysTempDir, dirName);
        srcDir = new File(tempDir, "src");
        testDir = new File(tempDir, "test-src");
        resourcesDir = new File(tempDir, "resources");
        templateDir = new File(resourcesDir, "templates");
        pluginXml = new File(resourcesDir, "atlassian-plugin.xml");

        tempDir.mkdirs();
        srcDir.mkdirs();
        resourcesDir.mkdirs();
        templateDir.mkdirs();

        InputStream is = this.getClass().getResourceAsStream("/empty-plugin.xml");
        IOUtils.copy(is, FileUtils.openOutputStream(pluginXml));

    }

    @After
    public void removeTempDir() throws IOException {
        FileUtils.deleteDirectory(tempDir);
    }

    protected Document getXmlDocument(File xmlFile) throws MalformedURLException, DocumentException {
        SAXReader reader = new SAXReader();
        return reader.read(xmlFile);
    }

    protected Properties loadI18nProperties() throws IOException {
        File i18nFile = new File(resourcesDir, AbstractPluginModuleCreator.DEFAULT_I18N_NAME + ".properties");
        Properties props = new Properties();

        InputStream is = null;
        try {
             is = FileUtils.openInputStream(i18nFile);
            props.load(is);

        }finally {
            IOUtils.closeQuietly(is);
        }

        return props;
    }
}
