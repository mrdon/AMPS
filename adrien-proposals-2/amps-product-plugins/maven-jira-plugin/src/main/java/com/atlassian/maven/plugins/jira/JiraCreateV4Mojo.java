package com.atlassian.maven.plugins.jira;

import com.atlassian.maven.plugins.amps.PluginInformation;
import org.apache.maven.plugin.MojoExecutionException;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoRequiresProject;

@MojoGoal ("create_v4")
@MojoRequiresProject (false)
public class JiraCreateV4Mojo extends JiraCreateMojo
{
    private static final String JIRA4 = "jira4";

    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return JIRA4;
    }

    @Override
    protected PluginInformation getPluginInformation()
    {
        return new PluginInformation(JIRA4, super.getPluginInformation().getVersion());
    }
}
