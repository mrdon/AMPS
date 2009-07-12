package com.atlassian.maven.plugins.amps.osgi;

import junit.framework.TestCase;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import static org.mockito.Mockito.mock;

import java.util.Map;
import java.util.HashMap;

/**
 *
 */
public class TestPackageImportRangeValidator extends TestCase
{
    public void testValidateAllImportsContainRanges() throws MojoFailureException
    {
        MavenProject project = mock(MavenProject.class);
        PackageImportVersionValidator validator = new PackageImportVersionValidator(project);

        validator.validate("foo.bar;version=\"[1.0,2.0)\"");

        validator.validate(null);
        validator.validate("");

        try
        {
            validator.validate("foo.bar;version=\"1.0\"");
        }
        catch (MojoFailureException ex)
        {
            // expected
        }

        try
        {
            validator.validate("foo.bar");
        }
        catch (MojoFailureException ex)
        {
            // expected
        }

        try
        {
            validator.validate("foo.bar;version=\"\"");
        }
        catch (MojoFailureException ex)
        {
            // expected
        }

        try
        {
            validator.validate("foo.bar;bar=foo");
        }
        catch (MojoFailureException ex)
        {
            // expected
        }
    }

    public void testCompressImports()
    {
        Map<String,String> imports = PackageImportVersionValidator.compressPackages(new HashMap<String,String>()
        {{
            put("foo.bar", "1.0");
            put("foo", "1.0");
            put("foo.bar.baz", "1.0");
            put("jim", "2.0");
        }});
        assertEquals(new HashMap<String,String>()
        {{
            put("foo*", "1.0");
            put("jim", "2.0");
        }}, imports);
    }

    public void testCompressImportsNotIncludeOutcast()
    {
        Map<String,String> imports = PackageImportVersionValidator.compressPackages(new HashMap<String,String>()
        {{
            put("foo.bar", "1.0");
            put("foo", "1.0");
            put("foo.bar.baz", "1.0");
            put("foo.jim", "2.0");
        }});
        assertEquals(new HashMap<String,String>()
        {{
            put("foo", "1.0");
            put("foo.bar*", "1.0");
            put("foo.jim", "2.0");
        }}, imports);
    }

    public void testCompressImportsNotAlwaysFullPackage()
    {
        Map<String,String> imports = PackageImportVersionValidator.compressPackages(new HashMap<String,String>()
        {{
            put("foo.bar", "1.0");
            put("foo.bar.baz", "1.0");
            put("foo.bara", "2.0");
        }});
        assertEquals(new HashMap<String,String>()
        {{
            put("foo.bar", "1.0");
            put("foo.bar.baz", "1.0");
            put("foo.bara", "2.0");
        }}, imports);
    }
}
