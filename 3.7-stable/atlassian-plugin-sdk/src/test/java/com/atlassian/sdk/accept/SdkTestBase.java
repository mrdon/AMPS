package com.atlassian.sdk.accept;

import junit.framework.TestCase;

import java.io.File;

public abstract class SdkTestBase extends TestCase
{
    protected File sdkHome;
    protected File baseDir;

    @Override
    protected void setUp() throws Exception
    {
        baseDir = new File(System.getProperty("sdk.base"));
        sdkHome = com.atlassian.sdk.accept.SdkHelper.setupSdk(baseDir);
    }
}
