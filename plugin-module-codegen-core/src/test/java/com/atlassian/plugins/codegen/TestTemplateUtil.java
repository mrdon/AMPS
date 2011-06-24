package com.atlassian.plugins.codegen;

import com.atlassian.plugins.codgen.util.CodeTemplateHelper;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * Author: jdoklovic
 */
public class TestTemplateUtil {

    @Test
    public void simpleTextReplacement() throws Exception {
        Properties props = new Properties();
        String expected = "i.work";
        props.setProperty("PACKAGE_NAME", expected);
        CodeTemplateHelper templateHelper = new CodeTemplateHelper();
        String parsedTemplate = templateHelper.parseTemplate("templates/simple/Simple.java.vm", props);

        assertEquals(expected, parsedTemplate);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void throwsResourceNotFound() throws Exception {
        Properties props = new Properties();
        String expected = "i.work";
        props.setProperty("PACKAGE_NAME", expected);

        CodeTemplateHelper templateHelper = new CodeTemplateHelper();
        String parsedTemplate = templateHelper.parseTemplate("/junk/simple/Simple.java.vm", props);

        assertEquals(expected, parsedTemplate);
    }
}
