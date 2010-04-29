package com.atlassian.maven.plugins.ampsdispatcher;

import org.jfrog.maven.annomojo.annotations.MojoGoal;

/**
 * Creates a test-resources compatible zip from the previous run's home dir
 *
 * @since 3.1-m3
 */
@MojoGoal("create-home-zip")
public class AmpsDispatcherCreateHomeZipMojo extends AbstractAmpsDispatcherMojo {
}
