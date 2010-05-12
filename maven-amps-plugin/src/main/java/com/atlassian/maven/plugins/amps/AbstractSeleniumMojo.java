package com.atlassian.maven.plugins.amps;

import org.apache.maven.plugin.MojoExecutionException;
import org.jfrog.maven.annomojo.annotations.MojoParameter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Mojo base class for Selenium related Mojos.
 */
public abstract class AbstractSeleniumMojo extends AbstractProductHandlerMojo
{
    @MojoParameter
    private Map selenium = new HashMap();

    public static final String SELENIUM_PACKAGE = "selenium";

     /**
     * The directory containing generated test classes of the project being tested.
     */
    @MojoParameter(expression = "${project.build.testOutputDirectory}", required = true)
    protected File testClassesDirectory;

    @MojoParameter(expression="${maven.test.skip}", defaultValue = "false")
    protected boolean testsSkip = false;

    @MojoParameter(expression="${skipTests}", defaultValue = "false")
    protected boolean skipTests = false;

    @Override
    protected void doExecute() throws MojoExecutionException
    {
        if (!hasSeleniumTests())
        {
            getLog().info("No selenium integration tests found in package '"+ SELENIUM_PACKAGE +"'. Not starting and stopping selenium server.");
            return;
        }
        if (testsSkip || skipTests)
        {
            getLog().info("Selenium Integration tests skipped - not starting and stopping selenium server.");
            return;
        }
        doExecute(selenium);
    }

    boolean hasSeleniumTests()
    {
        return new File(testClassesDirectory, AbstractSeleniumMojo.SELENIUM_PACKAGE).exists();
    }

    abstract void doExecute(final Map selenium) throws MojoExecutionException;
}
