package com.atlassian.maven.plugins.refapp;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;
import com.atlassian.maven.plugins.amps.FilterPluginDescriptorMojo;

/**
 *
 * @extendsPlugin amps
 * @goal filter-plugin-descriptor
 */
public class RefappFilterPluginDescriptorMojo extends FilterPluginDescriptorMojo
{
}
