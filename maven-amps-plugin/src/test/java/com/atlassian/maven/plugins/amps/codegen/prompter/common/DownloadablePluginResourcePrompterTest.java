package com.atlassian.maven.plugins.amps.codegen.prompter.common;

import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractPrompterTest;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.plugins.codegen.modules.common.DownloadablePluginResourceProperties;
import com.atlassian.plugins.codegen.modules.common.Resource;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @since version
 */
public class DownloadablePluginResourcePrompterTest extends AbstractPrompterTest {
    public static final String RESOURCE_NAME = "resourceCSS";
    public static final String RESOURCE_NAME_PATTERN = "templates/*.vm";
    public static final String DOWNLOAD_TYPE = "download";
    public static final String VELOCITY_TYPE = "velocity";
    public static final String RESOURCE_CSS_PATH = "templates/resource.css";
    public static final String RESOURCE_VM_PATH = "templates/resource.vm";
    public static final String PARAM_KEY = "paramKey";
    public static final String PARAM_VAL = "paramVal";


    @Test
    public void resourceWithNameIsValid() throws PrompterException {
        when(prompter.prompt("Enter Name (leave blank to use namePattern)")).thenReturn(RESOURCE_NAME);
        when(prompter.prompt("Enter Resource Type", "download")).thenReturn(DOWNLOAD_TYPE);
        when(prompter.prompt("Enter Location (path to resource file)")).thenReturn(RESOURCE_CSS_PATH);
        when(prompter.prompt("Add Resource Parameter?",PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("Y");
        when(prompter.prompt("params:\nparamKey->paramVal\nAdd Resource Parameter?",PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("param name")).thenReturn(PARAM_KEY);
        when(prompter.prompt("param value")).thenReturn(PARAM_VAL);
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        DownloadablePluginResourcePrompter modulePrompter = new DownloadablePluginResourcePrompter(prompter);
        DownloadablePluginResourceProperties props = (DownloadablePluginResourceProperties) modulePrompter.getModulePropertiesFromInput(moduleLocation);

        Resource cssResource = props.getResource();

        assertEquals("wrong css resource name",RESOURCE_NAME,cssResource.getName());
        assertTrue("css name pattern found when name is set", StringUtils.isBlank(cssResource.getNamePattern()));
        assertEquals("wrong css resource type", DOWNLOAD_TYPE,cssResource.getType());
        assertEquals("wrong css resource location",RESOURCE_CSS_PATH,cssResource.getLocation());
        assertEquals("wrong number of css resource params",1,cssResource.getParams().size());
        assertTrue("css resource param key not found", cssResource.getParams().containsKey(PARAM_KEY));
        assertEquals("wrong css resource param value", PARAM_VAL, cssResource.getParams().get(PARAM_KEY));
    }

    @Test
    public void resourceWithNamePatternIsValid() throws PrompterException {
        when(prompter.prompt("Enter Name (leave blank to use namePattern)")).thenReturn("");
        when(prompter.prompt("Enter Name Pattern")).thenReturn(RESOURCE_NAME_PATTERN);
        when(prompter.prompt("Enter Resource Type", "download")).thenReturn(VELOCITY_TYPE);
        when(prompter.prompt("Enter Location (path to resource file)")).thenReturn(RESOURCE_VM_PATH);
        when(prompter.prompt("Add Resource Parameter?",PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");
        when(prompter.prompt("Include Example Code?", PluginModulePrompter.YN_ANSWERS, "N")).thenReturn("N");

        DownloadablePluginResourcePrompter modulePrompter = new DownloadablePluginResourcePrompter(prompter);
        DownloadablePluginResourceProperties props = (DownloadablePluginResourceProperties) modulePrompter.getModulePropertiesFromInput(moduleLocation);

        Resource vmResource = props.getResource();

        assertTrue("vm name found when name pattern is set",StringUtils.isBlank(vmResource.getName()));
        assertEquals("wrong vm resource name pattern",RESOURCE_NAME_PATTERN,vmResource.getNamePattern());
        assertEquals("wrong vm resource type", VELOCITY_TYPE,vmResource.getType());
        assertEquals("wrong vm resource location", RESOURCE_VM_PATH, vmResource.getLocation());
        assertEquals("wrong number of vm resource params",0,vmResource.getParams().size());
    }
}
