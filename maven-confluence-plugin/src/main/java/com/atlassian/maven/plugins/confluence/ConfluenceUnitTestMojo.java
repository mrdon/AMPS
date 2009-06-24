package com.atlassian.maven.plugins.confluence;

import com.atlassian.maven.plugins.amps.UnitTestMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @extendsPlugin amps
 * @goal unit-test
 */
public class ConfluenceUnitTestMojo extends UnitTestMojo
{
    @Override
    protected String getProductId() throws MojoExecutionException
    {
        return "confluence";
    }
}
