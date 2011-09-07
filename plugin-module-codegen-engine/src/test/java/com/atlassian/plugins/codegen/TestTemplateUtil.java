package com.atlassian.plugins.codegen;

import com.atlassian.plugins.codegen.modules.BasicClassModuleProperties;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.util.CodeTemplateHelper;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class TestTemplateUtil {

    @Test
    public void simpleTextReplacement() throws Exception {
        PluginModuleProperties props = new BasicClassModuleProperties();
        String expected = "i.work";
        props.setProperty("PACKAGE_NAME", expected);
        CodeTemplateHelper templateHelper = new CodeTemplateHelper();
        String parsedTemplate = templateHelper.parseTemplate("templates/simple/Simple.java.vtl", props);

        assertEquals(expected, parsedTemplate);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void throwsResourceNotFound() throws Exception {
        PluginModuleProperties props = new BasicClassModuleProperties();
        String expected = "i.work";
        props.setProperty("PACKAGE_NAME", expected);

        CodeTemplateHelper templateHelper = new CodeTemplateHelper();
        String parsedTemplate = templateHelper.parseTemplate("/junk/simple/Simple.java.vtl", props);

        assertEquals(expected, parsedTemplate);
    }
}
