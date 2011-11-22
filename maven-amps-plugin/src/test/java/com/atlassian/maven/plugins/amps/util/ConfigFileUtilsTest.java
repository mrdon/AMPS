package com.atlassian.maven.plugins.amps.util;

import com.atlassian.maven.plugins.amps.util.ConfigFileUtils.Replacement;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConfigFileUtilsTest
{
    @Test
    public void replacementToStringReflectsWhenChangesTakeEffect()
    {
        assertEquals("A <-> B", new Replacement("A", "B").toString());

        assertEquals("A -> B", new Replacement("A", "B", false).toString());
        assertEquals("A <-> B", new Replacement("A", "B", true).toString());

        assertEquals("A <- B", new Replacement("A", "B", false, true).toString());
        assertEquals("A (nop) B", new Replacement("A", "B", false, false).toString());

        assertEquals("A -> B", new Replacement("A", "B", true, false).toString());
        assertEquals("A <-> B", new Replacement("A", "B", true, true).toString());
    }

    @Test
    public void factoryMethodForZippingMatchesConstructorBehaviour()
    {
        assertEquals(
                new Replacement("localhost", "server", true, false).toString(),
                Replacement.onlyWhenCreatingSnapshot("localhost", "server").toString());
    }
}
