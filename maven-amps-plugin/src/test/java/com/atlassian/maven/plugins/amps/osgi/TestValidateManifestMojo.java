package com.atlassian.maven.plugins.amps.osgi;

import junit.framework.TestCase;

import org.apache.maven.plugin.MojoFailureException;

/**
 *
 */
public class TestValidateManifestMojo extends TestCase
{
    public void testValidateAllImportsContainRanges() throws MojoFailureException
    {
        ValidateManifestMojo mojo = new ValidateManifestMojo();

        mojo.validateAllImportsContainRanges("foo.bar;version=\"[1.0,2.0)\"");

        mojo.validateAllImportsContainRanges(null);
        mojo.validateAllImportsContainRanges("");

        try
        {
            mojo.validateAllImportsContainRanges("foo.bar;version=\"1.0\"");
        }
        catch (MojoFailureException ex)
        {
            // expected
        }

        try
        {
            mojo.validateAllImportsContainRanges("foo.bar");
        }
        catch (MojoFailureException ex)
        {
            // expected
        }

        try
        {
            mojo.validateAllImportsContainRanges("foo.bar;version=\"\"");
        }
        catch (MojoFailureException ex)
        {
            // expected
        }

        try
        {
            mojo.validateAllImportsContainRanges("foo.bar;bar=foo");
        }
        catch (MojoFailureException ex)
        {
            // expected
        }
    }
}
