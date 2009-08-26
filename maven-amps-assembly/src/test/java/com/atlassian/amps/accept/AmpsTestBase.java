package com.atlassian.amps.accept;

import junit.framework.TestCase;

import java.io.File;

public abstract class AmpsTestBase extends TestCase
{
    protected File ampsHome;
    protected File baseDir;

    @Override
    protected void setUp() throws Exception
    {
        baseDir = new File(System.getProperty("amps.base"));
        ampsHome = com.atlassian.amps.accept.AmpsHelper.setupAmps(baseDir);
    }
}
