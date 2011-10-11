package com.atlassian.plugins.codegen.modules.common.moduletype;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;

import com.atlassian.plugins.codegen.AbstractCodegenTestCase;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

//TODO: update test to use Dom4J

/**
 * @since 3.6
 */
public class ModuleTypeTest extends AbstractCodegenTestCase<ModuleTypeProperties>
{
    public static final String PACKAGE_NAME = "com.atlassian.plugins.modules";

    @Before
    public void runGenerator() throws Exception
    {
        setCreator(new ModuleTypeModuleCreator());
        setModuleLocation(new PluginModuleLocation.Builder(srcDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(templateDir)
                .build());

        setProps(new ModuleTypeProperties(PACKAGE_NAME + ".DictionaryModuleDescriptor"));

        props.setFullyQualifiedInterface(PACKAGE_NAME + ".Dictionary");
        props.setIncludeExamples(false);

        creator.createModule(moduleLocation, props);
    }

    @Test
    public void allFilesAreGenerated() throws Exception
    {
        String packagePath = PACKAGE_NAME.replaceAll("\\.", Matcher.quoteReplacement(File.separator));
        String itPackagePath = "it" + File.separator + packagePath;
        assertTrue("interface class not generated", new File(srcDir, packagePath + File.separator + "Dictionary.java").exists());
        assertTrue("main class not generated", new File(srcDir, packagePath + File.separator + "DictionaryModuleDescriptor.java").exists());
        assertTrue("test class not generated", new File(testDir, packagePath + File.separator + "DictionaryModuleDescriptorTest.java").exists());
        assertTrue("funcTest class not generated", new File(testDir, itPackagePath + File.separator + "DictionaryModuleDescriptorFuncTest.java").exists());
        assertTrue("plugin.xml not generated", new File(resourcesDir, "atlassian-plugin.xml").exists());

    }

    @Test
    public void pluginXmlContainsModule() throws IOException
    {
        String pluginXmlContent = FileUtils.readFileToString(pluginXml);

        assertTrue("module not found in plugin xml", pluginXmlContent.contains("<module-type"));
        assertTrue("module class not found in plugin xml", pluginXmlContent.contains("class=\"" + PACKAGE_NAME + ".DictionaryModuleDescriptor\""));
    }

}
