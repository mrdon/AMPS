package com.atlassian.maven.plugins.ampsdispatcher;

import com.atlassian.maven.plugins.amps.RunMojo;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import org.apache.maven.plugin.MojoExecutionException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoExecute;
import org.jfrog.maven.annomojo.annotations.MojoRequiresDependencyResolution;

/**
 * Run the webapp
 */
@MojoGoal("run")
@MojoExecute(phase = "package")
@MojoRequiresDependencyResolution
public class AmpsDispatcherRunMojo extends AbstractAmpsDispatcherMojo
{
}

