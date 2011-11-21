package com.atlassian.maven.plugins.amps;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A group of tests to run for the given product identifiers
 *
 * @since 3.1
 */
public class TestGroup
{
    private String id;
    private List<String> instanceIds;
    private List<String> includes;
    private List<String> excludes;
    private Map<String, String> systemProperties;

    /**
     * Name of the folder in which integration test results should be written:
     * target/testGroup/classifier/surefire-reports. It used to always be tomcat6x.
     */
    private String classifier;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @deprecated As of 3.8, replaced by {@link TestGroup#setInstanceIds(List)}
     */
    public void setProductIds(List<String> products)
    {
        this.instanceIds = products;
    }

    public List<String> getInstanceIds()
    {
        return instanceIds;
    }

    public void setInstanceIds(List<String> instanceIds)
    {
        this.instanceIds = instanceIds;
    }



    public List<String> getIncludes()
    {
        return (includes == null) ? Collections.<String>emptyList() : includes;
    }

    /**
     * @param includes The file patterns to include
     */
    public void setIncludes(List<String> includes)
    {
        this.includes = includes;
    }

    public List<String> getExcludes()
    {
        return (excludes == null) ? Collections.<String>emptyList() : excludes;
    }

    /**
     * @param excludes The file patterns to exclude
     */
    public void setExcludes(List<String> excludes)
    {
        this.excludes = excludes;
    }
    public Map<String, String> getSystemProperties()
    {
        return (systemProperties == null) ? Collections.<String, String>emptyMap() : systemProperties;
    }
    public void setSystemProperties(Map<String, String> systemProperties)
    {
        this.systemProperties = systemProperties;
    }

    public String getClassifier()
    {
        return classifier;
    }

    public void setClassifier(String classifier)
    {
        this.classifier = classifier;
    }

}
