package com.atlassian.maven.plugins.amps;

public class PdkParams
{
    private final String pluginKey;
    private final String server;
    private final int port;
    private final String contextPath;
    private final String username;
    private final String password;
    private final String pluginFile;

    private PdkParams(String pluginKey, String server, int port, String contextPath, String username, String password, String pluginFile)
    {
        this.pluginKey = pluginKey;
        this.server = server;
        this.port = port;
        this.contextPath = contextPath;
        this.username = username;
        this.password = password;
        this.pluginFile = pluginFile;
    }

    public String getPluginKey()
    {
        return pluginKey;
    }

    public String getServer()
    {
        return server;
    }

    public int getPort()
    {
        return port;
    }

    public String getContextPath()
    {
        return contextPath;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getPluginFile()
    {
        return pluginFile;
    }

    public static class Builder
    {
        private String pluginKey;
        private String server;
        private int port;
        private String contextPath;
        private String username;
        private String password;
        private boolean testPlugin;

        public Builder pluginKey(String pluginKey)
        {
            this.pluginKey = pluginKey;
            return this;
        }

        public Builder server(String server)
        {
            this.server = server;
            return this;
        }

        public Builder port(int port)
        {
            this.port = port;
            return this;
        }

        public Builder contextPath(String contextPath)
        {
            this.contextPath = contextPath;
            return this;
        }

        public Builder username(String username)
        {
            this.username = username;
            return this;
        }

        public Builder password(String password)
        {
            this.password = password;
            return this;
        }

        public Builder testPlugin(boolean value)
        {
            this.testPlugin = value;
            return this;
        }


        public PdkParams build()
        {
            String pluginFile = "${project.build.directory}/${project.build.finalName}.jar";
            if (testPlugin)
            {
                pluginFile = "${project.build.directory}/${project.build.finalName}-tests.jar";
            }
            return new PdkParams(pluginKey, server, port, contextPath, username, password, pluginFile);
        }
    }
}
