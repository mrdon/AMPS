package com.atlassian.maven.plugins.refapp.pdk;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import com.atlassian.maven.plugins.amps.pdk.InstallMojo;

/**
 * @extendsPlugin amps
 * @goal install
 */
public class RefappInstallMojo extends InstallMojo
{
    @Override
    protected String getProductId() throws MojoExecutionException
    {
        return "refapp";
    }
}
