package com.atlassian.maven.plugins.refapp;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Debug the webapp
 *
 * @extendsPlugin amps
 * @requiresDependencyResolution debug
 * @goal debug
 * @execute phase="package"
 */
public class RefappDebugMojo extends RefappRunMojo
{
}
