package com.atlassian.maven.plugins.amps;



public class Jira extends Product
{
    public static final String ID = "jira";
    
    {
        // Default values for JIRA products
        id = ID;
        httpPort = 2990;
    }
    
    public void Jira(String e)
    {
        throw new RuntimeException("oh" + e);
    }
    
    @Override
    public String toString()
    {
        return "Jira" + id + " [instanceId=" + instanceId + ", localhost:" + httpPort + contextPath + "]";
    }

}
