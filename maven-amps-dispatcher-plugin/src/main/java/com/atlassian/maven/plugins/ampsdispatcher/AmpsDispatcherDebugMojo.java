package com.atlassian.maven.plugins.ampsdispatcher;

import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoExecute;
import org.jfrog.maven.annomojo.annotations.MojoRequiresDependencyResolution;

/**
 * Debug the webapp
 */
@MojoGoal("debug")
@MojoExecute(phase = "package")
@MojoRequiresDependencyResolution
public class AmpsDispatcherDebugMojo extends AbstractAmpsDispatcherMojo
{
}
